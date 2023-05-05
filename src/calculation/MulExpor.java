package calculation;

import code.CodeGenerator;
import code.TempVar;
import parse.Node;
import quaternion.BitAnd;
import quaternion.Div;
import quaternion.Mod;
import quaternion.Mul;

import java.util.ArrayList;

public class MulExpor {
//    public static int calMulExp(Node node) {
//        ArrayList<Node> sons = node.getSons();
//        int type = 0;
//        int result = UnaryExpor.calUnaryExp(sons.get(0));
//        int temp = 0;
//        for (int i = 1; i < sons.size(); i++) {
//            switch (sons.get(i).getName()) {
//                case "*":
//                    type = 0;
//                    break;
//                case "/":
//                    type = 1;
//                    break;
//                case "%":
//                    type = 2;
//                    break;
//                default: {
//                    temp = UnaryExpor.calUnaryExp(sons.get(i));
//                    if (type == 0) {
//                        result *= temp;
//                    } else if (type == 1) {
//                        result /= temp;
//                    } else {
//                        result = result % temp;
//                    }
//                }
//            }
//        }
//        return result;
//    }

    public static boolean mulExpResultIsNum(Node node) {
        ArrayList<Node> sons = node.getSons();
        Boolean result = UnaryExpor.unaryResultIsNum(sons.get(0));
        if (sons.size() == 1) {
            return result;
        }
        for (int i = 1; i < sons.size(); i++) {
            switch (sons.get(i).getName()) {
                case "*":
                    break;
                case "/":
                    break;
                case "%":
                    break;
                default: {
                    Boolean temp = UnaryExpor.unaryResultIsNum(sons.get(i));
                    if (!(result && temp)) { //纯数字
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static TempVar calMulExpvr(Node node) {
        ArrayList<Node> sons = node.getSons();
        int type = 0;
        TempVar result = UnaryExpor.calUnaryExpvr(sons.get(0));
        for (int i = 1; i < sons.size(); i++) {
            switch (sons.get(i).getName()) {
                case "*":
                    type = 0;
                    break;
                case "/":
                    type = 1;
                    break;
                case "%":
                    type = 2;
                    break;
                case "bitand":
                    type = 3;
                    break;
                default: {
                    TempVar temp = UnaryExpor.calUnaryExpvr(sons.get(i));
                    if (type == 0) {
                        if (AddExpor.isPureNum(result.getName()) && AddExpor.isPureNum(temp.getName())) { //纯数字直接加和
                            int numRuslt = Integer.parseInt(result.getName()) * Integer.parseInt(temp.getName());
                            TempVar tempVar = new TempVar(Integer.toString(numRuslt));
                            result = tempVar;
                        } else {
                            TempVar tempVar = new TempVar();
                            Mul mul = new Mul(tempVar.getName(), result.getName(), temp.getName());
                            CodeGenerator.quaternions.add(mul);
                            result = tempVar;
                        }

                    } else if (type == 1) {
                        if (AddExpor.isPureNum(result.getName()) && AddExpor.isPureNum(temp.getName())) {
                            int numRuslt = Integer.parseInt(result.getName()) / Integer.parseInt(temp.getName());
                            TempVar tempVar = new TempVar(Integer.toString(numRuslt));
                            result = tempVar;
                        } else {
                            TempVar tempVar = new TempVar();
                            Div div = new Div(tempVar.getName(), result.getName(), temp.getName());
                            CodeGenerator.quaternions.add(div);
                            result = tempVar;
                        }
                    } else if (type == 2) {
                        if (AddExpor.isPureNum(result.getName()) && AddExpor.isPureNum(temp.getName())) {
                            int numRuslt = Integer.parseInt(result.getName()) % Integer.parseInt(temp.getName());
                            TempVar tempVar = new TempVar(Integer.toString(numRuslt));
                            result = tempVar;
                        } else {
                            TempVar tempVar = new TempVar();
                            Mod mod = new Mod(tempVar.getName(), result.getName(), temp.getName());
                            CodeGenerator.quaternions.add(mod);
                            result = tempVar;
                        }
                    } else {
                        TempVar tempVar = new TempVar();
                        BitAnd bitAnd = new BitAnd(tempVar.getName(), result.getName(), temp.getName());
                        CodeGenerator.quaternions.add(bitAnd);
                        result = tempVar;
                    }
                }
            }
        }
        return result;
    }

}
