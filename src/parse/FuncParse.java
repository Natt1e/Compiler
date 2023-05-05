package parse;

import java.util.ArrayList;

public class FuncParse extends Gramma{


    public FuncParse(ArrayList<String> words, ArrayList<String> kind, ArrayList<Integer> rows) {
        super(words, kind, rows);
    }

    public static void FuncDef(Node father) {
        Node type = new Node(words.get(index),rows.get(index),true);
        father.addSon(type);
        PrintToTxt(words.get(index));// type
        gPrint("FuncType");
        Node ident = new Node(words.get(index),rows.get(index),false);
        father.addSon(ident);
        PrintToTxt(words.get(index));//Ident
        PrintToTxt(words.get(index));//(
        if (!words.get(index).equals(")")) {
            if (!words.get(index).equals("{")) {
                Node funcFParams = new Node("FuncFParams",rows.get(index),true);
                father.addSon(funcFParams);
                FuncFParams(funcFParams);
            } else {
                Node funcFParams = new Node("NoParams",rows.get(index),true);
                father.addSon(funcFParams);
            }
        } else {
            Node funcFParams = new Node("NoParams",rows.get(index),true);
            father.addSon(funcFParams);
        }
        if (words.get(index).equals(")")) {
            PrintToTxt(words.get(index));//)
        } else {
            Node err = new Node(")error",rows.get(index-1),true);
            father.addSon(err);
        }
        Node block = new Node("Block",rows.get(index),true);
        father.addSon(block);
        Block(block);
        gPrint("FuncDef");
    }

    public static void FuncFParams(Node father) {//形参表
        Node node = new Node("FuncFParam",rows.get(index),true);
        father.addSon(node);
        FuncFParam(node);
        while (words.get(index).equals(",")) {
            PrintToTxt(words.get(index));// ,
            Node node1 = new Node("FuncFParam",rows.get(index),true);
            father.addSon(node1);
            FuncFParam(node1);
        }
        gPrint("FuncFParams");
    }

    public static void FuncFParam(Node father) {//形参
        Node node = new Node(words.get(index),rows.get(index),true);
        father.addSon(node);
        PrintToTxt(words.get(index));//Btype
        Node node1 = new Node(words.get(index),rows.get(index),false);
        father.addSon(node1);
        PrintToTxt(words.get(index));//Ident
        if (words.get(index).equals("[")) {
            Node node2 = new Node("[",rows.get(index),true);
            father.addSon(node2);
            PrintToTxt(words.get(index));//[
            if (!words.get(index).equals("]")) {
                Node node3 = new Node("]error",rows.get(index-1),true);
                father.addSon(node3);
            } else {
                Node node3 = new Node("]",rows.get(index),true);
                father.addSon(node3);
                PrintToTxt(words.get(index));//]
            }
            if (words.get(index).equals("[")) {
                Node node4 = new Node("[",rows.get(index),true);
                father.addSon(node4);
                PrintToTxt(words.get(index));//[
                Node node5 = new Node("ConstExp",rows.get(index),true);
                father.addSon(node5);
                ExpParse.ConstExp(node5);
                if (!words.get(index).equals("]")) {
                    Node node6 = new Node("]error",rows.get(index-1),true);
                    father.addSon(node6);
                } else {
                    PrintToTxt(words.get(index));//]
                    Node node6 = new Node("]",rows.get(index),true);
                    father.addSon(node6);
                }
            }
        }
        //FuncNameTable.getLastPT().addParamTable(array,node1.getName()); //为 “函数形参表” 添加 “表项”
        gPrint("FuncFParam");
    }

    public static void FuncRParams(Node father) {//实参表
        Node node = new Node("Exp",rows.get(index),true);
        father.addSon(node);
        ExpParse.Exp(node);
        while (words.get(index).equals(",")) {
            PrintToTxt(words.get(index));
            Node node1 = new Node("Exp",rows.get(index),true);
            father.addSon(node1);
            ExpParse.Exp(node1);
        }
        gPrint("FuncRParams");
    }

}
