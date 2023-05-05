import parse.Node;
import table.FuncNameTable;
import table.FuncParamTable;
import table.SymbolTable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Error {
    private final Node ancestor;
    private static final String path = "error.txt";
    private static final boolean print = true;
    private static File writefile = new File(path);
    private static SymbolTable ancestorTable;
    private static SymbolTable curTable;
    private static ArrayList<ErrItem> errors;
    public static boolean errorSwitch = false;

    public Error(Node ancestor) {
        this.ancestor = ancestor;// 祖宗节点
        ancestorTable = new SymbolTable(null);
        curTable = ancestorTable; // 当前符号表为祖宗符号表
        errors = new ArrayList<>();
        if (!writefile.exists()) {
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writefile.delete();
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//处理文件,若文件不存在则创建,若文件存在则删除重建
    }

    public static void printErrToTxt() {
        errors.sort(Comparator.comparingInt(ErrItem::getRow));
        for (int i = 0; i < errors.size(); i++) {
            if (i + 1 < errors.size()) {
                if (errors.get(i).getRow() == errors.get(i + 1).getRow()) {
                    errors.remove(i + 1);
                    i--;
                }
            }
        }
        if (print) {
            for (ErrItem error : errors) {
                try {
                    FileWriter fw = new FileWriter(writefile, true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    fw.write(error.toString() + '\n');
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void ErrorMain() {
        find(this.ancestor);
        printErrToTxt();
    }

    public void find(Node node) {
        ArrayList<Node> sons = node.getSons();
        int i;
        if (node.getName().equals(";error") && node.getSys()) { // 处理没有分号
            printErr(node.getRow(), "i");
            node.setName(";");
        } else if (node.getName().equals("]error") && node.getSys()) {   // 处理缺少 “]”
            printErr(node.getRow(), "k");
            node.setName("]");
        } else if (node.getName().equals(")error") && node.getSys()) {   // 处理缺少 “)”
            printErr(node.getRow(), "j");
            node.setName(")");
        } else if (node.getName().equals("FormatString") && node.getSys()) {
            if (!formatErr(node.getSons().get(0).getName(), node.getSons().get(0).getRow())) { // 处理非法符号
                //若返回为假,则判断是否会出现 “printf中格式字符与表达式个数不匹配”的问题
                int dSum = findDSum(node.getSons().get(0).getName());
                ArrayList<Node> bros = node.getDad().getSons();
                int expSum = 0;
                for (Node bro : bros) {
                    if (bro.getName().equals("Exp") && bro.getSys()) {
                        expSum++;
                    }
                }
                if (expSum != dSum) { // 个数不匹配，报错！！！
                    printErr(node.getDad().getSons().get(0).getRow(), "l");
                }
            }
        } else if ((node.getName().equals("continue") || node.getName().equals("break")) && node.getSys()) {
            Node father = node.getDad();
            boolean err = true;
            while (!father.getName().equals("FuncDef") && !father.getName().equals("MainFuncDef")) {
                if (father.getName().equals("Stmt") && node.getSys()) {
                    ArrayList<Node> bros = father.getDad().getSons();
                    if (bros.get(0).getName().equals("while") && node.getSys()) {
                        err = false;
                        break;
                    }
                }
                father = father.getDad();
            }
            if (err) {
                printErr(node.getRow(), "m");
            }
        } else if (node.getName().equals("Block") && node.getSys()) {
            if (!node.getDad().getName().equals("FuncDef") && node.getSys()) {
                curTable = new SymbolTable(curTable);
            }
        } else if (node.getName().equals("ConstDef") && node.getSys()) {
            int row = node.getSons().get(0).getRow();
            int type = 0;
            int num = 0;
            for (int k = 0; k < node.getSons().size(); k++) {
                if (node.getSons().get(k).getName().equals("[") && node.getSys()) {
                    num++;
                }
            }
            if (num == 2) {
                type = -1;
            } else if (num == 1) {
                type = 1;
            }
            if (!curTable.addSymbol(node.getSons().get(0).getName(), type, true)) {
                printErr(row, "b");
            }
        } else if (node.getName().equals("VarDef") && node.getSys()) {
            int row = node.getSons().get(0).getRow();
            int type = 0;
            int num = 0;
            for (int k = 0; k < node.getSons().size(); k++) {
                if (node.getSons().get(k).getName().equals("[") && node.getSys()) {
                    num++;
                }
            }
            if (num == 2) {
                type = -1;
            } else if (num == 1) {
                type = 1;
            }
            if (!curTable.addSymbol(node.getSons().get(0).getName(), type, false)) {
                printErr(row, "b");
            }
        } else if (node.getName().equals("LVal") && node.getSys()) {
            int row = node.getSons().get(0).getRow();
            String name = node.getSons().get(0).getName();
            if (!curTable.findSymbol(name) && FuncNameTable.ifFunc(name) == -1) {
                printErr(row, "c");
            } else {
                if (node.getDad().getName().equals("Stmt") && node.getSys()) {
                    if (node.getDad().getSons().get(1).getName().equals("=") && node.getDad().getSons().get(1).getSys()) {
                        if ((node.getDad().getSons().get(2).getName().equals("Exp") || node.getDad().getSons().get(2).getName().equals("getint"))
                         && node.getDad().getSons().get(2).getSys()) {
                            if (curTable.ifConst(name)) {
                                printErr(row, "h");
                            }
                        }
                    }
                }
            }
        } else if (node.getName().equals("UnaryExp") && node.getSys()) {
            String name = node.getSons().get(0).getName();
            if (!name.equals("UnaryOp") && !name.equals("PrimaryExp")) {
                int row = node.getSons().get(0).getRow();
                if (!curTable.findSymbol(name) && FuncNameTable.ifFunc(name) == -1) {
                    printErr(row, "c");
                } else {
                    int actual = 0;
                    int form = FuncNameTable.getParamSum(name);
                    if (node.getSons().get(2).getName().equals("FuncRParams") && node.getSons().get(2).getSys()) {
                        actual = node.getSons().get(2).getSons().size();
                    }
                    if (actual != form) {
                        printErr(row, "d");
                    } else {
                        if (actual != 0) {
                            judgeParamsType(name, node, row);
                        }
                        // 此处为类型不匹配预留位置
                        // dddddddddddddddddd
                    }
                }

            }
        } else if (node.getName().equals("FuncDef") && node.getSys()) {
            Node type = node.getSons().get(0);
            Node ident = node.getSons().get(1);
            if (curTable.findSymbol(ident.getName()) || FuncNameTable.ifFunc(ident.getName()) != -1) {
                printErr(ident.getRow(), "b");
            }
            FuncNameTable.addFuncName(type.getName(), ident.getName()); // 为 “函数名表” 添加 “表项”
            addParamToFuncParamTable(node.getSons().get(2));

            curTable = new SymbolTable(curTable);
            //处理函数内部的符号表
            addFuncSym(node);
            if (node.getSons().get(0).getName().equals("int")) {
                Node block = null;
                ArrayList<Node> sons1 = node.getSons();
                for (int t = 1; t < sons1.size(); t++) {
                    if (sons1.get(t).getName().equals("Block") && sons1.get(t).getSys()) {
                        block = sons1.get(t);
                        break;
                    }
                }
                int last = block.getSons().size() - 1;
                int row = block.getSons().get(last).getRow();
                if (block.getSons().size() == 1) {
                    printErr(row, "g");
                } else {
                    if (!block.getSons().get(last - 1).getSons().get(0).getSons().get(0).getName().equals("return")
                    && block.getSons().get(last - 1).getSons().get(0).getSons().get(0).getSys()) {
                        printErr(row, "g");
                    }
                }
            } else {
                Node block = null;
                ArrayList<Node> sons1 = node.getSons();
                for (int t = 1; t < sons1.size(); t++) {
                    if (sons1.get(t).getName().equals("Block") && sons1.get(t).getSys()) {
                        block = sons1.get(t);
                        break;
                    }
                }
                findAllReturns(block);
            }
        } else if (node.getName().equals("MainFuncDef") && node.getSys()) {
            Node block = node.getSons().get(0);
            int last = block.getSons().size() - 1;
            int row = block.getSons().get(last).getRow();
            if (block.getSons().size() == 1) {
                printErr(row, "g");
            } else {
                if (!block.getSons().get(last - 1).getSons().get(0).getSons().get(0).getName().equals("return") &&
                        block.getSons().get(last - 1).getSons().get(0).getSons().get(0).getSys()) {
                    printErr(row, "g");
                }
            }
        }

        for (i = 0; i < sons.size(); i++) {
            find(sons.get(i));
        }

        if (node.getName().equals("Block") && node.getSys()) {
            if (!node.getDad().getName().equals("FuncDef")) {
                curTable = curTable.getFather();
            }
        } else if (node.getName().equals("FuncDef") && node.getSys()) {

            curTable = curTable.getFather();
        }
    }

    public void addParamToFuncParamTable(Node node) {
        if (node.getName().equals("NoParams") && node.getSys()) {
            return;
        }
        int size = node.getSons().size();
        for (int i = 0; i < size; i++) {
            Node node1 = node.getSons().get(i);
            int array = 0;
            if (node1.getSons().size() == 7) {
                array = -1;
            } else if (node1.getSons().size() == 4) {
                array = 1;
            }
            FuncNameTable.getLastPT().addParamTable(array, node1.getSons().get(1).getName()); //为 “函数形参表” 添加 “表项”
        }

    }

    public void findAllReturns(Node node) {
        int size = node.getSons().size(); //Block
        if (node.getName().equals("return")) {
            if (node.getDad().getSons().get(1).getName().equals("Exp") && node.getDad().getSons().get(1).getSys()) {
                int row = node.getRow();
                printErr(row, "f");
            }
        }

        for (int i = 0; i < size; i++) {
            findAllReturns(node.getSons().get(i));
        }
    }

    public int findDSum(String mom) { //此函数为统计 formatString 中 “%d” 的个数
        int len = mom.length();
        int num = 0;
        for (int i = 0; i < len; i++) {
            if (mom.charAt(i) == '%') {
                num++;
                i++;
            }
        }
        return num;
    }


    public boolean formatErr(String s, int row) { // 返回真为出现了问题
        if (s.charAt(0) != '"' || s.charAt(s.length() - 1) != '"') {
            printErr(row, "a");
            return true;
        } else {
            String temp = s.substring(1, s.length() - 1);
            int len = temp.length();
            for (int i = 0; i < len; i++) {
                if (temp.charAt(i) == '%') { // 处理 %d
                    if (i + 1 >= len) {
                        printErr(row, "a");
                        return true;
                    } else {
                        if (temp.charAt(i + 1) != 'd') {
                            printErr(row, "a");
                            return true;
                        } else {
                            i++; // 成功匹配%d 跳过d的判断
                        }
                    }
                } else {
                    if (temp.charAt(i) == '\\') {
                        if (i + 1 >= len) {
                            printErr(row, "a");
                            return true;
                        } else {
                            if (temp.charAt(i + 1) != 'n') {
                                printErr(row, "a");
                                return true;
                            } else {
                                i++; // \n 匹配成功 跳过n的判断
                            }
                        }
                    } else {
                        if (!(temp.charAt(i) == ' ' || temp.charAt(i) == '!' ||
                                (temp.charAt(i) >= 40 && temp.charAt(i) <= 126))) {
                            printErr(row, "a");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void printErr(int row, String s) {
        ErrItem errItem = new ErrItem(row, s);
        errors.add(errItem);

    }




    public void addFuncSym(Node node) {
        String funcName = node.getSons().get(1).getName();
        SymbolTable table1 = curTable;
        FuncParamTable table2 = FuncNameTable.getParams(funcName);
        for (int i = 0; i < table2.getSum(); i++) {
            if (!table1.addSymbol(table2.getName(i), table2.getType(i), false)) {
                int row = node.getSons().get(1).getRow();
                printErr(row, "b");
            }
        }
    }

    public void judgeParamsType(String name, Node node, int row) {
        FuncParamTable table = FuncNameTable.getParams(name);
        int size = table.getSum();
        Node node1 = node.getSons().get(2);
        for (int i = 0; i < size; i++) {
            int actualType = 0;
            int type = table.getType(i);
            Node temp = node1.getSons().get(i); //Exp
            while (true) {
                temp = temp.getSons().get(0); //  AddExp;
                temp = temp.getSons().get(0); // MulExp;
                temp = temp.getSons().get(0); //UnaryExp;
                if (temp.getSons().get(0).getName().equals("PrimaryExp") && temp.getSons().get(0).getSys()) {
                    temp = temp.getSons().get(0); //PrimaryExp
                    if (temp.getSons().get(0).getName().equals("Number") && temp.getSons().get(0).getSys()) {
                        break;
                    } else if (temp.getSons().get(0).getName().equals("LVal") && temp.getSons().get(0).getSys()) {
                        temp = temp.getSons().get(0); //LVal
                        String ident = temp.getSons().get(0).getName();
                        if (!curTable.findSymbol(ident) && FuncNameTable.ifFunc(ident) == -1) {
                            printErr(temp.getSons().get(0).getRow(), "c");
                            return;
                        }
                        if (temp.getSons().size() == 1) { // 没有 [ ]
                            actualType = curTable.getType(ident);
                            break;
                        } else if (temp.getSons().size() == 4) { // 一个[]
                            actualType = curTable.getType(ident);
                            if (actualType > 0) {
                                actualType = 0;
                            } else if (actualType < 0) {
                                actualType = 1;
                            }
                            break;
                        } else { // 两个 []
                            break;
                        }
                    } else {
                        temp = temp.getSons().get(1);
                    }
                } else if (temp.getSons().get(0).getName().equals("UnaryOp") && temp.getSons().get(0).getSys()) {
                     // 必为常数
                    break;
                } else {
                    String funcName = temp.getSons().get(0).getName();
                    if (FuncNameTable.ifFunc(funcName) == -1) {
                        printErr(temp.getSons().get(0).getRow(), "c");
                    }
                    if (FuncNameTable.getFuncType(funcName).equals("void")) { // 返回值类型是void直接报错
                        printErr(row, "e");
                        return;
                    } else {
                         // 返回值类型为int
                        break;
                    }
                }
            }
            if (type != actualType) {
                printErr(row, "e");
                return;
            }
        }
    }
}
