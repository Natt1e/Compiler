package parse;


import java.util.ArrayList;

public class ExpParse extends Gramma {


    public ExpParse(ArrayList<String> words, ArrayList<String> kind, ArrayList<Integer> rows) {
        super(words, kind, rows);
    }

    public static void Exp(Node father) {
        Node node = new Node("AddExp",rows.get(index),true);
        father.addSon(node);
        AddExp(node);
        gPrint("Exp");
    }

    public static void AddExp(Node father) {
        Node node = new Node("MulExp",rows.get(index),true);
        father.addSon(node);
        MulExp(node);
        while (words.get(index).equals("+") || words.get(index).equals("-")) {
            gPrint("AddExp");
            Node node1 = new Node(words.get(index),rows.get(index),true);
            father.addSon(node1);
            PrintToTxt(words.get(index));// + or -
            Node node2 = new Node("MulExp",rows.get(index),true);
            father.addSon(node2);
            MulExp(node2);
        }
        gPrint("AddExp");
    }

    public static void MulExp(Node father) {
        Node node = new Node("UnaryExp",rows.get(index),true);
        father.addSon(node);
        UnaryExp(node);
        while (words.get(index).equals("*") || words.get(index).equals("/") || words.get(index).equals("%") || words.get(index).equals("bitand")) {
            gPrint("MulExp");
            Node node1 = new Node(words.get(index),rows.get(index),true);
            father.addSon(node1);
            PrintToTxt(words.get(index));// * or / or % or bitand
            Node node2 = new Node("UnaryExp",rows.get(index),true);
            father.addSon(node2);
            UnaryExp(node2);
        }
        gPrint("MulExp");
    }

    public static void UnaryExp(Node father) {
        if (words.get(index).equals("(")) {
            Node node = new Node("PrimaryExp",rows.get(index),true);
            father.addSon(node);
            PrimaryExp(node);
        } else if (isNumber(words.get(index).charAt(0))) {
            Node node = new Node("PrimaryExp",rows.get(index),true);
            father.addSon(node);
            PrimaryExp(node);
        } else if (words.get(index).equals("+") || words.get(index).equals("-") || words.get(index).equals("!")) {
            Node node = new Node("UnaryOp",rows.get(index),true);
            father.addSon(node);
            Node node1 = new Node(words.get(index),rows.get(index),true);
            node.addSon(node1);
            PrintToTxt(words.get(index));// + or - or !
            gPrint("UnaryOp");
            Node node2 = new Node("UnaryExp",rows.get(index),true);
            father.addSon(node2);
            UnaryExp(node2);
        } else {
            /*
            ！！！！！！！！！！！！！！
            * */
            if (words.get(index + 1).equals("(")) {
                Node node = new Node(words.get(index),rows.get(index),false);
                father.addSon(node);
                PrintToTxt(words.get(index)); //Ident
                Node node1 = new Node("(",rows.get(index),true);
                father.addSon(node1);
                PrintToTxt(words.get(index));//(
                if (findright()) {  //本行未出现问题
                    if (!words.get(index).equals(")")) {
                        Node node2 = new Node("FuncRParams",rows.get(index),true);
                        father.addSon(node2);
                        FuncParse.FuncRParams(node2);
                    }
                    Node node3 = new Node(")",rows.get(index),true);
                    father.addSon(node3);
                    PrintToTxt(words.get(index));//)
                } else { //本行出现问题
                    if (words.get(index).equals(")")) {
                        Node node3 = new Node(")",rows.get(index),true);
                        father.addSon(node3);
                        PrintToTxt(words.get(index));//)
                    } else {
                        if (!words.get(index).equals(";")) {
                            Node node2 = new Node("FuncRParams",rows.get(index),true);
                            father.addSon(node2);
                            FuncParse.FuncRParams(node2);
                        }

                        Node node3 = new Node(")error",rows.get(index-1),true);
                        father.addSon(node3);
                    }

                }
            } else {
                Node node = new Node("PrimaryExp",rows.get(index),true);
                father.addSon(node);
                PrimaryExp(node);
            }
        }
        gPrint("UnaryExp");
    }

    public static boolean findright() {
        int j = index;
        int row = rows.get(index);
        int b = 0;
        while (rows.get(j) == row) {
            j--;
            if (j == -1) {
                break;
            }
        }
        j += 1;
        while (rows.get(j) == row) {
            if (words.get(j).equals(")")) {
                b--;
            } else if (words.get(j).equals("(")) {
                b++;
            }
            j++;
        }
        return true;
//        return b == 0;
    }

    public static void PrimaryExp(Node father) {
        if (isNumber(words.get(index).charAt(0))) {
            Node node = new Node("Number",rows.get(index),true);
            father.addSon(node);
            Number(node);
        }else if (words.get(index).equals("(")) {
            Node node = new Node("(",rows.get(index),true);
            father.addSon(node);
            PrintToTxt(words.get(index));
            Node node3 = new Node("Exp",rows.get(index),true);
            father.addSon(node3);
            Node node1 = new Node("AddExp",rows.get(index),true);
            node3.addSon(node1);
            AddExp(node1);
            gPrint("Exp");
            Node node2 = new Node(")",rows.get(index),true);
            father.addSon(node2);
            PrintToTxt(words.get(index));//)

        }else {
            Node node = new Node("LVal",rows.get(index),true);
            father.addSon(node);
            LVal(node);
        }
        gPrint("PrimaryExp");
    }

    public static void Number(Node father) {
        Node node = new Node(words.get(index),rows.get(index),false);
        father.addSon(node);
        IntConst();
        gPrint("Number");
    }

    public static void LVal(Node father) {
        Node node = new Node(words.get(index),rows.get(index),false);
        father.addSon(node);
        PrintToTxt(words.get(index));//Ident
        while (words.get(index).equals("[")) {
            Node node1 = new Node("[",rows.get(index),true);
            father.addSon(node1);
            PrintToTxt(words.get(index));//[
            Node node2 = new Node("AddExp",rows.get(index),true);
            father.addSon(node2);
            AddExp(node2);
            gPrint("Exp");
            if (!words.get(index).equals("]")) {
                Node node3 = new Node("]error",rows.get(index),true);
                father.addSon(node3);
            } else {
                Node node3 = new Node("]",rows.get(index),true);
                father.addSon(node3);
                PrintToTxt(words.get(index));
            }

        }
        gPrint("LVal");
    }

    public static void IntConst() {
        PrintToTxt(words.get(index));
    }

    public static void ConstExp(Node father) {
        Node node = new Node("AddExp",rows.get(index),true);
        father.addSon(node);
        AddExp(node);
        gPrint("ConstExp");
    }

}
