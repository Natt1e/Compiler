package code;
import Cond.CondCounter.IfCounter;
import Cond.CondCounter.WhileCounter;
import Cond.LOrExpHandler;
import Cond.WhileStack;
import actuator.Actuator;
import actuator.MainActuator;
import actuator.StringActuator;
import calculation.AddExpor;
import parse.Node;
import quaternion.*;
import quaternion.Jump.Jump;
import quaternion.Label.*;
import table.AliasTable;
import table.SymbolTable;
import java.util.ArrayList;
import java.util.HashMap;

/*
中间代码生成器
 */
public class CodeGenerator {
    private final Node ancestor;
    private final SymbolTable ancestorTable; //全局符号表
    public static int addr;
    public static SymbolTable curTable;
    private final boolean mediatePrint = true; //输出中间代码的开关
    public static ArrayList<Quaternion> quaternions;
    public static ArrayList<Quaternion> globalConsts;
    public static HashMap<Integer,SymbolTable> symbolTableMap;
    public static AliasTable aliasTable;//重命名符号表

    public CodeGenerator(Node node) {
        this.ancestor = node;
        this.ancestorTable = new SymbolTable(null);
        addr = 0; //内存初始地址
        CodeGenerator.curTable = ancestorTable;
        CodeGenerator.aliasTable = new AliasTable();
        quaternions = new ArrayList<>();
        globalConsts = new ArrayList<>();
        symbolTableMap = new HashMap<>();
        symbolTableMap.put(BlockCounter.getBlockCount(),curTable); //全局符号表为 0号符号表
    }

    public void generate() {
        Actuator.initialization(); // 初始化: 1.创建文件 2.输出.data

        GlobalCodeGenerator.processGlobal(ancestor);// 初始化全局变量
        TempCounter.reset();

        for (Node son : ancestor.getSons()) {
            if (!son.getName().equals("Decl") && son.getSys()) {
                StringActuator.processStr(son); // 初始化 字符串
            }
        }
        StringActuator.printStrs();


        Actuator.writeToMips(".text");
        Actuator.writeToMips("li $fp, 0x10040000");
        Actuator.writeToMips("li $gp, 0x10040000");
        Actuator.writeToMips("j func.main");

        for (Node son : ancestor.getSons()) { //处理函数体
            if (son.getName().equals("FuncDef")) {
                SymbolTable table = new SymbolTable(curTable);
                curTable = table;
                addr = 0;

                FuncGenerator.generateFuncBlock(son);

                curTable = table.getFather();
                addr = 0;
            }
        }

        Actuator.writeToMips("func.main:");
        ArrayList<Node> sons = ancestor.getSons();
        ergodicMainTree(sons.get(sons.size() - 1));
        MainActuator.generateMIPS(false);
        printMediate();
    }


    public static void ergodicMainTree(Node node) { // 主函数
        ArrayList<Node> sons = node.getSons();
        boolean stmtFlag = false;
        if (node.getName().equals("Stmt") && node.getSys() &&
                !node.getSons().get(0).getName().equals("Block")) {   // Stmt
            stmtFlag = true;
            workWithStmt(node);
        } else if (node.getName().equals("Block") && node.getSys()) {   // block
            if (!node.getDad().getName().equals("FuncDef") && node.getSys()) {
                SymbolTable table = new SymbolTable(curTable);
                curTable = table;
                int blockId = BlockCounter.getBlockCount();
                node.setId(blockId);
                symbolTableMap.put(blockId,table);
            }
        } else if (node.getName().equals("ConstDecl") && node.getSys()) { //常量声明
            workWithConstDef(node.getSons());
        } else if (node.getName().equals("VarDecl") && node.getSys()) {  //变量声明
            workWithVarDef(node.getSons());
        }

        if (!stmtFlag) {
            for (Node son : sons) {
                ergodicMainTree(son);
            }
        }

        if (node.getName().equals("Block") && node.getSys()) {
            if (!node.getDad().getName().equals("FuncDef")) {
                curTable = curTable.getFather();
            }
        }
    }

    public static void workWithStmt(Node node) {
        if (node.getSons().get(0).getName().equals("LVal") && node.getSys()) {
            if (node.getSons().get(2).getName().equals("getint")) {     // 输入语句
                workWithGetin(node.getSons().get(0));
            } else {
                workWithAssaign(node.getSons().get(0));                 // 赋值语句
            }
        } else if (node.getSons().get(0).getName().equals("printf")) {  //输出语句
            workWithPrint(node.getSons().get(0));
        } else if (node.getSons().get(0).getName().equals("Exp")) {     // Exp;
            AddExpor.calAddExpvr(node.getSons().get(0));
            End end = new End();
            quaternions.add(end);
        } else if (node.getSons().get(0).getName().equals("return")) {  // return返回语句
            workWithReturn(node.getSons().get(0));
        } else if (node.getSons().get(0).getName().equals("if")) {      // 处理if语句
            IfCounter.getNewIfNo();
            IfBegin ifBegin = new IfBegin();
            IfEnd ifEnd = new IfEnd();
            ElseEnd elseEnd = new ElseEnd();

            LOrExpHandler.HandleLOrExp(node.getSons().get(2), ifBegin.getLabel(), ifEnd.getLabel(), true);

            CodeGenerator.quaternions.add(ifBegin);
            ergodicMainTree(node.getSons().get(4));

            if (node.getSons().size() > 5) {   // 有else
                Jump jump = new Jump(elseEnd.getLabel());
                CodeGenerator.quaternions.add(jump);
            }

            CodeGenerator.quaternions.add(ifEnd);

            if (node.getSons().size() > 5) {   // 有else
                ergodicMainTree(node.getSons().get(6));
                CodeGenerator.quaternions.add(elseEnd);
            }
        } else if (node.getSons().get(0).getName().equals("while")) {
            WhileCounter.getNewIfNo();
            WhileStack.push(WhileCounter.getCurrentNo());

            WhileBegin whileBegin = new WhileBegin();
            WhileBody whileBody = new WhileBody();
            WhileEnd whileEnd = new WhileEnd();
            CodeGenerator.quaternions.add(whileBegin);

            LOrExpHandler.HandleLOrExp(node.getSons().get(2), whileBody.getLabel(), whileEnd.getLabel(), false);

            CodeGenerator.quaternions.add(whileBody);
            ergodicMainTree(node.getSons().get(4));

            Jump jump = new Jump(whileBegin.getLabel());
            CodeGenerator.quaternions.add(jump);
            CodeGenerator.quaternions.add(whileEnd);

            WhileStack.pop();

        } else if (node.getSons().get(0).getName().equals("break")) {
            Jump jump = new Jump("WhileEnd_" + WhileStack.getCurent());
            CodeGenerator.quaternions.add(jump);
        } else if (node.getSons().get(0).getName().equals("continue")) {
            Jump jump = new Jump("WhileBegin_" + WhileStack.getCurent());
            CodeGenerator.quaternions.add(jump);
        }
    }

    public static void workWithReturn(Node node) {
        if (node.getDad().getSons().get(1).getName().equals("Exp")) {
            Node exp = node.getDad().getSons().get(1);
            Return re = new Return(AddExpor.calAddExpvr(exp).getName());
            quaternions.add(re);
        } else {
            Return re = new Return("0");
            quaternions.add(re);
        }
    }

    public static void workWithPrint(Node node) {
        ArrayList<Node> bros = node.getDad().getSons();
        Node formatString = null;
        ArrayList<Node> exps = new ArrayList<>();
        for (Node son : bros) {
            if (son.getName().equals("FormatString")) {
                formatString = son;
            } else if (son.getName().equals("Exp")) {
                exps.add(son);
            }
        }
        PrintGenerator.processPrintf(formatString,exps);
    }

    public static void workWithAssaign(Node node) {
        String identName = node.getSons().get(0).getName();
        Node exp = node.getDad().getSons().get(2);
        if (node.getSons().size() == 1) {       //为普通变量赋值
            Assign assign = new Assign(curTable.getAlias(identName), AddExpor.calAddExpvr(exp).getName(), "", "", 0);
            quaternions.add(assign);
        } else if (node.getSons().size() == 4) {      //  为一维数组赋值
            Node d1 = node.getSons().get(2);
            Assign assign = new Assign(curTable.getAlias(identName), AddExpor.calAddExpvr(exp).getName(), AddExpor.calAddExpvr(d1).getName(),"", 1);
            quaternions.add(assign);
        } else {                                    //  为二维数组赋值
            Node d1 = node.getSons().get(2);
            Node d2 = node.getSons().get(5);
            Assign assign = new Assign(curTable.getAlias(identName), AddExpor.calAddExpvr(exp).getName(),
                    AddExpor.calAddExpvr(d1).getName(),AddExpor.calAddExpvr(d2).getName(), 2);
            quaternions.add(assign);
        }
        End end = new End();
        quaternions.add(end);
    }

    public static void workWithGetin(Node lval) {
        ArrayList<Node> sons = lval.getSons();
        String identName = curTable.getAlias(sons.get(0).getName());
        if (sons.size() == 1) {         // 普通变量
            GetIn gi = new GetIn(identName, 0, null, null);
            quaternions.add(gi);
        } else if (sons.size() == 4) {  // 一维数组变量
            GetIn gi = new GetIn(identName, 1, AddExpor.calAddExpvr(sons.get(2)).getName(), null);
            quaternions.add(gi);
        } else {                        // 二维数组变量
            GetIn gi = new GetIn(identName, 2,
                    AddExpor.calAddExpvr(sons.get(2)).getName(), AddExpor.calAddExpvr(sons.get(5)).getName());
            quaternions.add(gi);
        }
    }

    public static void workWithConstDef(ArrayList<Node> constDefs) {
        int k = 2;
        while (constDefs.get(k).getName().equals("ConstDef")) {
            int dimension = 0;
            ArrayList<Node> sons = constDefs.get(k).getSons();//ConstDef的儿子
            for (Node node : sons) {
                if (node.getName().equals("[")) {
                    dimension++;
                }
            }
            String identName = sons.get(0).getName();
            if (dimension == 0) { //普通常量
                GlobalCodeGenerator.addVarToTable(identName,true,0,0,false);
                AliasTable.addValue(CodeGenerator.curTable.getAlias(identName),Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName()));
            } else if (dimension == 1) { // 一维常量数组
                int d1 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName());
                GlobalCodeGenerator.addVarToTable(identName, true, d1, 0, false);
                ArrayList<Integer> values = new ArrayList<>();
                Node constInitval = sons.get(5);
                ArrayList<Node> constExps = constInitval.getSons();
                VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 1);
                for (Node exp : constExps) {
                    if (exp.getName().equals("ConstInitVal")) {
                        Node number = exp.getSons().get(0);
                        String initialNum = AddExpor.calAddExpvr(number).getName();
                        values.add(Integer.parseInt(initialNum));
                        vd.addValue(initialNum);
                    }
                }
                addr += d1;
                AliasTable.addValues(CodeGenerator.curTable.getAlias(identName), values);
                quaternions.add(vd);
            } else if (dimension == 2) { // 二维常量数组
                int d1 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName());
                int d2 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(5)).getName());
                GlobalCodeGenerator.addVarToTable(identName, true, d1, d2, false);
                ArrayList<Integer> values = new ArrayList<>();
                Node constInitVal = sons.get(8);
                ArrayList<Node> constInitVals = constInitVal.getSons();
                VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 2);

                for (Node initVal : constInitVals) {
                    if (initVal.getName().equals("ConstInitVal")) {
                        for (Node exp : initVal.getSons()) {
                            if (exp.getName().equals("ConstInitVal")) {
                                Node number = exp.getSons().get(0);
                                String initNum = AddExpor.calAddExpvr(number).getName();
                                values.add(Integer.parseInt(initNum));
                                vd.addValue(initNum);
                            }
                        }
                    }
                }

                addr += d1 * d2;
                AliasTable.addValues(CodeGenerator.curTable.getAlias(identName), values);
                quaternions.add(vd);
            }
            k++;
        }
    }

    public static void workWithVarDef(ArrayList<Node> sons) {
        int k = 1;
        while (sons.get(k).getName().equals("VarDef")) {
            int dimension = 0;
            ArrayList<Node> defSons = sons.get(k).getSons();
            String identName = defSons.get(0).getName();
            for (Node node : defSons) {
                if (node.getName().equals("[")) {
                    dimension++;
                }
            }
            if (dimension == 0) {
                // 定义普通变量
                boolean isGetint = false;
                GlobalCodeGenerator.addVarToTable(identName,false,0,0,false);
                VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 0);
                if (defSons.size() == 3) {      // 有初始值的定义
                     vd.addValue(AddExpor.calAddExpvr(defSons.get(2)).getName());
                } else if (defSons.size() == 5) { //处理getint
                    isGetint = true;
                }
                quaternions.add(vd);
                End end = new End();
                quaternions.add(end);
                if (isGetint) {
                    GetIn gi = new GetIn(CodeGenerator.curTable.getAlias(identName), 0, null, null);
                    quaternions.add(gi);
                }
                addr += 1;
            } else if (dimension == 1) {
                // 定义一维数组
                int d1 = Integer.parseInt(AddExpor.calAddExpvr(defSons.get(2)).getName());
                GlobalCodeGenerator.addVarToTable(identName,false,d1,0,false);
                VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 1);
                if (defSons.size() == 6) {      // 有初始值的定义
                    Node initVal = defSons.get(5);
                    for (Node init : initVal.getSons()) {
                        if (init.getName().equals("InitVal")) {
                            vd.addValue(AddExpor.calAddExpvr(init.getSons().get(0)).getName());
                            End end = new End();
                            quaternions.add(end);
                        }
                    }
                }
                quaternions.add(vd);
                addr += d1;
            } else if (dimension == 2) {
                //定义二维数组
                int d1 = Integer.parseInt(AddExpor.calAddExpvr(defSons.get(2)).getName());
                int d2 = Integer.parseInt(AddExpor.calAddExpvr(defSons.get(5)).getName());
                GlobalCodeGenerator.addVarToTable(identName,false,d1, d2,false);
                VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 2);

                if (defSons.size() == 9) {  // 有初始值的定义
                    Node init1 = defSons.get(8);
                    for (Node init2 : init1.getSons()) {
                        if (init2.getName().equals("InitVal")) {
                            for (Node init3 : init2.getSons()) {
                                if (init3.getName().equals("InitVal")) {
                                    vd.addValue(AddExpor.calAddExpvr(init3.getSons().get(0)).getName());
                                    End end = new End();
                                    quaternions.add(end);
                                }
                            }
                        }
                    }
                }
                quaternions.add(vd);
                addr += d1 * d2;
            }
            k++;
            /*
            GlobalCodeGenerator.addVarToTable(identName,false,0,0,false);
            if (sons1.size() == 3) {
                Assign assign = new Assign(curTable.getAlias(identName),AddExpor.calAddExpvr(sons1.get(2)).getName());
                assign.setisDef(true);
                quaternions.add(assign);
                End end = new End();
                quaternions.add(end);
            } else {
                Assign assign = new Assign(curTable.getAlias(identName),"0");
                assign.setisDef(true);
                quaternions.add(assign);
            }
            addr++;
            k++;
             */
        }
    }


    public void printMediate() { //输出中间代码
        if (mediatePrint) {
            for (Quaternion quaternion : quaternions) {
                System.out.println(quaternion.toString());
            }
        }
    }

}
