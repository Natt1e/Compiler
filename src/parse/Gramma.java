package parse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class  Gramma {
    public static ArrayList<String> words;//词法分析程序分析出的单词表
    public static int index;//单词表的索引
    private static final boolean print = false;//控制语法分析器输出的信号量
    private static final String path = "output.txt";
    private static FileWriter fw;
    private static BufferedWriter bw;
    private static File writefile = new File(path);
    public static ArrayList<String> kind;
    public static ArrayList<Integer> rows;
    private Node ancestor = new Node("ancestor",-1,true);

    public Gramma(ArrayList<String> words,ArrayList<String> kind,ArrayList<Integer> rows) {
        Gramma.words = words;
        Gramma.kind = kind;
        index = 0;
        Gramma.rows = rows;
    }

    public Node getAncestor() {
        return this.ancestor;
    }

    public void CompUnit() {
        while (true) {//处理全局变量 Decl
            if (words.get(index).equals("const")) {
                Node node = new Node("Decl",-1,true);
                ancestor.addSon(node);
                DeclParse.Decl(node);
            } else if (!words.get(index + 2).equals("(")) {
                Node node = new Node("Decl",-1,true);
                ancestor.addSon(node);
                DeclParse.Decl(node);
            } else {
                break;
            }
        }
        while (true) {//处理函数声明 FuncDef
            if (words.get(index).equals("void")) {
                Node node = new Node("FuncDef",-1,true);
                ancestor.addSon(node);
                FuncParse.FuncDef(node);
            } else if (!words.get(index + 1).equals("main")) {
                Node node = new Node("FuncDef",-1,true);
                ancestor.addSon(node);
                FuncParse.FuncDef(node);
            } else {
                break;
            }
        }
        Node node = new Node("MainFuncDef",-1,true);
        ancestor.addSon(node);
        MainFuncDef(node);
        gPrint("CompUnit");
    }

    public void MainFuncDef(Node father) {
        PrintToTxt(words.get(index));//int
        PrintToTxt(words.get(index));//main
        PrintToTxt(words.get(index));//(
        PrintToTxt(words.get(index));//)
        Node block = new Node("Block",-1,true);
        father.addSon(block);
        Block(block);
        gPrint("MainFuncDef");
    }

    public static void Block(Node father) {

        PrintToTxt(words.get(index));// {
        while (!words.get(index).equals("}")) {
            Node item = new Node("BlockItem",-1,true);
            father.addSon(item);
            BlockItem(item);
        }
        Node node = new Node(words.get(index), rows.get(index),true);
        father.addSon(node);
        PrintToTxt(words.get(index));//}
        gPrint("Block");
    }

    public static void BlockItem(Node father) {
        if (words.get(index).equals("int") || words.get(index).equals("const")) {
            Node node = new Node("Decl",-1,true);
            father.addSon(node);
            DeclParse.Decl(node);
        } else {
            Node node = new Node("Stmt",-1,true);
            father.addSon(node);
            Stmt(node);
        }
    }

    public static void FormatString(Node father) {
        Node node = new Node(words.get(index), rows.get(index),false);
        father.addSon(node);
        PrintToTxt(words.get(index));//FormatString
    }

    public static void Stmt(Node father) {
        switch (words.get(index)) {
            case "{" : {
                Node block = new Node("Block", -1, true);
                father.addSon(block);
                Block(block);//Block
                break;
            }
            case "if" : {
                Node node1 = new Node("if", rows.get(index), true);
                father.addSon(node1);
                PrintToTxt(words.get(index));//if

                Node node2 = new Node("(", rows.get(index), true);
                father.addSon(node2);
                PrintToTxt(words.get(index));//(

                Node node3 = new Node("Cond", -1, true);
                father.addSon(node3);
                CondParse.Cond(node3);
                if (!words.get(index).equals(")")) {
                    Node node4 = new Node(")error", rows.get(index - 1), true);
                    father.addSon(node4);
                } else {
                    Node node4 = new Node(")", rows.get(index), true);
                    father.addSon(node4);
                    PrintToTxt(words.get(index));//)

                }
                Node node5 = new Node("Stmt", -1, true);
                father.addSon(node5);
                Stmt(node5);
                if (words.get(index).equals("else")) {
                    Node node6 = new Node("else", -1, true);
                    father.addSon(node6);
                    PrintToTxt(words.get(index));//else
                    Node node7 = new Node("Stmt", -1, true);
                    father.addSon(node7);
                    Stmt(node7);
                }
                break;
            }
            case "while" : {
                Node node1 = new Node("while", -1, true);
                father.addSon(node1);
                PrintToTxt(words.get(index));//while

                Node node2 = new Node("(", -1, true);
                father.addSon(node2);
                PrintToTxt(words.get(index));//(

                Node node3 = new Node("Cond", -1, true);
                father.addSon(node3);
                CondParse.Cond(node3);
                if (!words.get(index).equals(")")) {
                    Node node4 = new Node(")error", rows.get(index - 1), true);
                    father.addSon(node4);
                } else {
                    PrintToTxt(words.get(index));//)
                    Node node4 = new Node(")", -1, true);
                    father.addSon(node4);
                }
                Node node5 = new Node("Stmt", -1, true);
                father.addSon(node5);
                Stmt(node5);
                break;
            }
            case "break" : {
                Node node1 = new Node("break", rows.get(index), true);
                father.addSon(node1);
                PrintToTxt(words.get(index));//break

                if (!words.get(index).equals(";")) {
                    Node node2 = new Node(";error", rows.get(index - 1), true);
                    father.addSon(node2);
                } else {
                    Node node2 = new Node(";", -1, true);
                    father.addSon(node2);
                    PrintToTxt(words.get(index));//;
                }
                break;
            }
            case "continue" : {
                Node node1 = new Node("continue", rows.get(index), true);
                father.addSon(node1);
                PrintToTxt(words.get(index));//continue

                if (!words.get(index).equals(";")) {
                    Node node2 = new Node(";error", rows.get(index - 1), true);
                    father.addSon(node2);
                } else {
                    Node node2 = new Node(";", -1, true);
                    father.addSon(node2);
                    PrintToTxt(words.get(index));//;
                }
                break;
            }
            case "return" : {
                Node node1 = new Node("return", rows.get(index), true);
                father.addSon(node1);
                PrintToTxt(words.get(index));//return

                if (!words.get(index).equals(";") && !words.get(index).equals("}")) {
                    if (rows.get(index).equals(rows.get(index - 1))) {
                        Node node = new Node("Exp", rows.get(index), true);
                        father.addSon(node);
                        ExpParse.Exp(node);
                    }

                }
                if (!words.get(index).equals(";")) {
                    Node node = new Node(";error", rows.get(index - 1), true);
                    father.addSon(node);
                } else {
                    Node node = new Node(";", rows.get(index), true);
                    father.addSon(node);
                    PrintToTxt(words.get(index));//;
                }
                break;
            }
            case "printf" : {
                Node node = new Node("printf", rows.get(index), true);
                father.addSon(node);
                Node node1 = new Node("(", rows.get(index), true);
                father.addSon(node1);
                PrintToTxt(words.get(index));//printf

                PrintToTxt(words.get(index));//(

                Node node2 = new Node("FormatString", rows.get(index), true);
                father.addSon(node2);
                FormatString(node2);
                while (words.get(index).equals(",")) {
                    PrintToTxt(words.get(index));//,
                    Node node3 = new Node("Exp", rows.get(index), true);
                    father.addSon(node3);
                    ExpParse.Exp(node3);
                }
                if (!words.get(index).equals(")")) {
                    Node node3 = new Node(")error", rows.get(index - 1), true);
                    father.addSon(node3);
                } else {
                    PrintToTxt(words.get(index));//)
                    Node node3 = new Node(")", rows.get(index), true);
                    father.addSon(node3);
                }
                if (!words.get(index).equals(";")) {
                    Node node3 = new Node(";error", rows.get(index - 1), true);
                    father.addSon(node3);
                } else {
                    PrintToTxt(words.get(index));//;
                    Node node3 = new Node(";", rows.get(index), true);
                    father.addSon(node3);
                }
                break;
            }
            default : {
                int j = index;
                boolean ifExp = true;
                while (!words.get(j).equals(";") && rows.get(index).equals(rows.get(j))) {
                    if (words.get(j).equals("=")) {
                        ifExp = false;
                        break;
                    }
                    j++;
                }
                if (!ifExp) {
                    Node node = new Node("LVal", rows.get(index), true);
                    father.addSon(node);
                    ExpParse.LVal(node);
                    Node node3 = new Node("=", rows.get(index), true);
                    father.addSon(node3);
                    PrintToTxt(words.get(index));//=
                    if (words.get(index).equals("getint")) {
                        Node node4 = new Node("getint", rows.get(index), true);
                        father.addSon(node4);
                        PrintToTxt(words.get(index));
                        Node node5 = new Node("(", rows.get(index), true);
                        father.addSon(node5);
                        PrintToTxt(words.get(index));//(
                        if (!words.get(index).equals(")")) {
                            Node node6 = new Node(")error", rows.get(index - 1), true);
                            father.addSon(node6);
                        } else {
                            Node node6 = new Node(")", rows.get(index), true);
                            father.addSon(node6);
                            PrintToTxt(words.get(index));//)
                        }

                    } else {
                        Node node4 = new Node("Exp", rows.get(index), true);
                        father.addSon(node4);
                        ExpParse.Exp(node4);
                    }
                } else {
                    if (!words.get(index).equals(";")) {
                        Node node = new Node("Exp", rows.get(index), true);
                        father.addSon(node);
                        ExpParse.Exp(node);
                    }
                }
                if (!words.get(index).equals(";")) {
                    Node node3 = new Node(";error", rows.get(index - 1), true);
                    father.addSon(node3);
                } else {
                    Node node3 = new Node(";", rows.get(index), true);
                    father.addSon(node3);
                    PrintToTxt(words.get(index));//;
                }
                break;
            }
        }

        gPrint("Stmt");
    }


    public static void PrintToTxt(String s) {
        if (print) {
            try {
                fw = new FileWriter(writefile, true);
                bw = new BufferedWriter(fw);
                fw.write(kind.get(index) + s + '\n');
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        index++;
    }

    public static void gPrint(String s) {
        if (print) {
            try {
                    fw = new FileWriter(writefile, true);
                    bw = new BufferedWriter(fw);
                    fw.write("<" + s + ">" + '\n');
                    fw.flush();
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

}
