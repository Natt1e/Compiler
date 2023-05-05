package parse;

import java.util.ArrayList;

public class CondParse extends Gramma {


    public CondParse(ArrayList<String> words, ArrayList<String> kind, ArrayList<Integer> rows) {
        super(words, kind, rows);
    }

    public static void Cond(Node father) {
        LOrExp(father);
        gPrint("Cond");
    }

    public static void LOrExp(Node father) {
        Node node = new Node("LAndExp",rows.get(index),true);
        father.addSon(node);
        LAndExp(node);
        while (words.get(index).equals("||")) {
            gPrint("LOrExp");
            Node or = new Node("||",rows.get(index),true);
            father.addSon(or);
            PrintToTxt(words.get(index));// ||
            Node node1 = new Node("LAndExp",rows.get(index),true);
            father.addSon(node1);
            LAndExp(node1);
        }
        gPrint("LOrExp");
    }

    public static void LAndExp(Node father) {
        Node node1 = new Node("EqExp",rows.get(index),true);
        father.addSon(node1);
        EqExp(node1);
        while (words.get(index).equals("&&")) {
            gPrint("LAndExp");
            Node and = new Node("&&",rows.get(index),true);
            father.addSon(and);
            PrintToTxt(words.get(index));//&&
            Node node2 = new Node("EqExp",rows.get(index),true);
            father.addSon(node2);
            EqExp(node2);
        }
        gPrint("LAndExp");
    }

    public static void EqExp(Node father) {
        Node node1 = new Node("RelExp",rows.get(index),true);
        father.addSon(node1);
        RelExp(node1);
        while (words.get(index).equals("==") || words.get(index).equals("!=")) {
            gPrint("EqExp");
            Node equal = new Node(words.get(index),rows.get(index),true);
            father.addSon(equal);
            PrintToTxt(words.get(index));// == !=
            Node node2 = new Node("RelExp",rows.get(index),true);
            father.addSon(node2);
            RelExp(node2);
        }
        gPrint("EqExp");
    }

    public static void RelExp(Node father) {
        Node node1 = new Node("AddExp",rows.get(index),true);
        father.addSon(node1);
        ExpParse.AddExp(node1);
        while (words.get(index).equals("<") || words.get(index).equals(">") ||
                words.get(index).equals("<=") || words.get(index).equals(">=")) {
            gPrint("RelExp");
            Node sym = new Node(words.get(index),rows.get(index),true);
            father.addSon(sym);
            PrintToTxt(words.get(index));//< > <= >=
            Node node2 = new Node("AddExp",rows.get(index),true);
            father.addSon(node2);
            ExpParse.AddExp(node2);
        }
        gPrint("RelExp");
    }

}
