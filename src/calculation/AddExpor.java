package calculation;
import code.CodeGenerator;
import code.TempVar;
import parse.Node;
import quaternion.Add;
import quaternion.Sub;

import java.util.ArrayList;

public class AddExpor {

//    public static int calAddExp(Node node) {
//        while (!node.getName().equals("AddExp")) {
//            node = node.getSons().get(0);
//        }
//        boolean isAdd = true;
//        ArrayList<Node> sons = node.getSons();
//        int result = MulExpor.calMulExp(sons.get(0));
//        int temp = 0;
//        for (int i = 1; i < sons.size(); i++) {
//            if (sons.get(i).getName().equals("-")) {
//                isAdd = false;
//            } else if (sons.get(i).getName().equals("+")) {
//                isAdd = true;
//            } else {
//                temp = MulExpor.calMulExp(sons.get(i));
//                if (isAdd) {
//                    result += temp;
//                } else {
//                    result -= temp;
//                }
//            }
//        }
//        return result;
//    }

    public static Boolean addExpResultIsNum(Node node) {
        while (!node.getName().equals("AddExp")) {
            node = node.getSons().get(0);
        }
        ArrayList<Node> sons = node.getSons();
        Boolean result = MulExpor.mulExpResultIsNum(sons.get(0));
        if (sons.size() == 1) {
            return result;
        }
        for (int i = 1; i < sons.size(); i++) {
            if (!sons.get(i).getName().equals("-") && !sons.get(i).getName().equals("+")) {
                Boolean temp = MulExpor.mulExpResultIsNum(sons.get(i));
                if (!(result && temp)) { //纯数字直接加和
                    return false;
                }
            }
        }
        return true;
    }

    public static TempVar calAddExpvr(Node node) {
        while (!node.getName().equals("AddExp")) {
            node = node.getSons().get(0);
        }
        boolean isAdd = true;
        ArrayList<Node> sons = node.getSons();
        TempVar result = MulExpor.calMulExpvr(sons.get(0));
        for (int i = 1; i < sons.size(); i++) {
            if (sons.get(i).getName().equals("-")) {
                isAdd = false;
            } else if (sons.get(i).getName().equals("+")) {
                isAdd = true;
            } else {
                TempVar temp = MulExpor.calMulExpvr(sons.get(i));
                if (isAdd) {
                    if (isPureNum(result.getName()) && isPureNum(temp.getName())) { //纯数字直接加和
                        int numRuslt = Integer.parseInt(result.getName()) + Integer.parseInt(temp.getName());
                        TempVar tempVar = new TempVar(Integer.toString(numRuslt));
                        result = tempVar;
                    } else {
                        TempVar tempVar = new TempVar();
                        Add add = new Add(tempVar.getName(), result.getName(), temp.getName());
                        CodeGenerator.quaternions.add(add);
                        result = tempVar;
                    }
                } else {
                    if (isPureNum(result.getName()) && isPureNum(temp.getName())) { //纯数字直接加和
                        int numRuslt = Integer.parseInt(result.getName()) - Integer.parseInt(temp.getName());
                        TempVar tempVar = new TempVar(Integer.toString(numRuslt));
                        result = tempVar;
                    } else {
                        TempVar tempVar = new TempVar();
                        Sub sub = new Sub(tempVar.getName(), result.getName(), temp.getName());
                        CodeGenerator.quaternions.add(sub);
                        result = tempVar;
                    }

                }
            }
        }
        return result;
    }

    public static boolean isPureNum(String nums) {
        try {
            Integer.parseInt(nums);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
