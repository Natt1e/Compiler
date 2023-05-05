package parse;

import java.util.ArrayList;

public class DeclParse extends Gramma {

    public DeclParse(ArrayList<String> words, ArrayList<String> kind, ArrayList<Integer> rows) {
        super(words, kind, rows);
    }

    public static void Decl(Node father) {
        if (words.get(index).equals("const")) {
            Node node = new Node("ConstDecl",rows.get(index),true);
            father.addSon(node);
            ConstDecl(node);
        } else {
            Node node = new Node("VarDecl",rows.get(index),true);
            father.addSon(node);
            VarDecl(node);
        }
    }

    public static void ConstDecl(Node father) {
        Node node1 = new Node("const",rows.get(index),true);
        father.addSon(node1);
        PrintToTxt(words.get(index));//const
        Node node2 = new Node("int",rows.get(index),true);
        father.addSon(node2);
        PrintToTxt(words.get(index));//int
        Node node3 = new Node("ConstDef",rows.get(index),true);
        father.addSon(node3);
        ConstDef(node3);
        while (words.get(index).equals(",")) {
            PrintToTxt(words.get(index));// ,
            Node node4 = new Node("ConstDef",rows.get(index),true);
            father.addSon(node4);
            ConstDef(node4);
        }
        if (!words.get(index).equals(";")) {
            Node err = new Node(";error",rows.get(index-1),true);
            father.addSon(err);
        } else {
            Node temp = new Node(";",rows.get(index),true);
            father.addSon(temp);
            PrintToTxt(words.get(index));//;
        }
        gPrint("ConstDecl");
    }

    public static void ConstDef(Node father) {//常数定义
        Node ident = new Node(words.get(index),rows.get(index),false);
        father.addSon(ident);
        PrintToTxt(words.get(index));//Ident
        while (words.get(index).equals("[")) {
            Node node1 = new Node("[",rows.get(index),true);
            father.addSon(node1);
            PrintToTxt(words.get(index));//输出[
            Node node2 = new Node("ConstExp",rows.get(index),true);
            father.addSon(node2);
            ExpParse.ConstExp(node2);
            if (!words.get(index).equals("]")) {
                Node node3 = new Node("]error",rows.get(index-1),true);
                father.addSon(node3);
            } else {
                Node node3 = new Node("]",rows.get(index),true);
                father.addSon(node3);
                PrintToTxt(words.get(index));//输出]
            }
        }
        Node equal = new Node("=",rows.get(index),true);
        father.addSon(equal);
        PrintToTxt(words.get(index));// =
        Node node = new Node("ConstInitVal",rows.get(index),true);
        father.addSon(node);
        ConstInitVal(node);
        gPrint("ConstDef");
    }

    public static void ConstInitVal(Node father) {//常量初值
        if (words.get(index).equals("{")) {
            Node node = new Node("{",rows.get(index),true);
            father.addSon(node);
            PrintToTxt(words.get(index));//{
            if (!words.get(index).equals("}")) {
                Node node1 = new Node("ConstInitVal",rows.get(index),true);
                father.addSon(node1);
                ConstInitVal(node1);
                while (words.get(index).equals(",")) {
                    PrintToTxt(words.get(index));// ,
                    Node node2 = new Node("ConstInitVal",rows.get(index),true);
                    father.addSon(node2);
                    ConstInitVal(node2);
                }
            }
            Node node3 = new Node("}",rows.get(index),true);
            father.addSon(node3);
            PrintToTxt(words.get(index));//}
        }else {
            Node node = new Node("ConstExp",rows.get(index),true);
            father.addSon(node);
            ExpParse.ConstExp(node);
        }
        gPrint("ConstInitVal");
    }

    public static void VarDecl(Node father) {//变量声明
        Node node = new Node("int",rows.get(index),true);
        father.addSon(node);
        PrintToTxt(words.get(index));// int
        Node node1 = new Node("VarDef",rows.get(index),true);
        father.addSon(node1);
        VarDef(node1);
        while (words.get(index).equals(",")) {
            PrintToTxt(words.get(index));//,
            Node node2 = new Node("VarDef",rows.get(index),true);
            father.addSon(node2);
            VarDef(node2);
        }
        if (!words.get(index).equals(";")) {
            Node node3 = new Node(";error",rows.get(index-1),true);
            father.addSon(node3);
        } else {
            PrintToTxt(words.get(index));// ;
            Node node3 = new Node(";",rows.get(index),true);
            father.addSon(node3);
        }

        gPrint("VarDecl");
    }

    public static void VarDef(Node father) {//变量定义
        Node ident = new Node(words.get(index),rows.get(index),false);
        father.addSon(ident);
        PrintToTxt(words.get(index));//Ident
        while (words.get(index).equals("[")) {
            Node node = new Node("[",rows.get(index),true);
            father.addSon(node);
            PrintToTxt(words.get(index));// [
            Node node1 = new Node("ConstExp",rows.get(index),true);
            father.addSon(node1);
            ExpParse.ConstExp(node1);
            if (!words.get(index).equals("]")) {
                Node node2 = new Node("]error",rows.get(index-1),true);
                father.addSon(node2);
            } else {
                Node node2 = new Node("]",rows.get(index),true);
                father.addSon(node2);
                PrintToTxt(words.get(index));// ]
            }
        }
        if (words.get(index).equals("=")) {
            Node node3 = new Node("=",rows.get(index),true);
            father.addSon(node3);
            PrintToTxt(words.get(index)); // =
            if (words.get(index).equals("getint")) {
                Node node = new Node("getint", rows.get(index), true);
                father.addSon(node);
                Node node1 = new Node("(", rows.get(index), true);
                Node node2 = new Node(")", rows.get(index), true);
                father.addSon(node1);
                father.addSon(node2);
                index += 3;
            } else {
                Node node4 = new Node("InitVal",rows.get(index),true);
                father.addSon(node4);
                InitVal(node4);
            }

        }
        gPrint("VarDef");
    }

    public static void InitVal(Node father) { //变量初值
        if (words.get(index).equals("{")) {
            Node node = new Node("{",rows.get(index),true);
            father.addSon(node);
            PrintToTxt(words.get(index));// {
            if (!words.get(index).equals("}")) {
                Node node1 = new Node("InitVal",rows.get(index),true);
                father.addSon(node1);
                InitVal(node1);
                while (words.get(index).equals(",")) {
                    PrintToTxt(words.get(index));// ,
                    Node node2 = new Node("InitVal",rows.get(index),true);
                    father.addSon(node2);
                    InitVal(node2);
                }
            }
            Node node1 = new Node("}",rows.get(index),true);
            father.addSon(node1);
            PrintToTxt(words.get(index));//}
        } else {
            Node node = new Node("Exp",rows.get(index),true);
            father.addSon(node);
            ExpParse.Exp(node);
        }
        gPrint("InitVal");
    }

}
