package calculation;

import actuator.Actuator;
import actuator.GlobalActuator;
import code.CodeGenerator;
import code.TempVar;
import parse.Node;
import quaternion.*;
import table.AliasTable;
import table.FuncNickTable;

import java.util.ArrayList;

public class UnaryExpor {

//    public static int calUnaryExp(Node node) {
//        ArrayList<Node> sons = node.getSons();
//        if (sons.get(0).getName().equals("UnaryOp") && sons.get(0).getSys()) {
//            if (sons.get(0).getSons().get(0).getName().equals("-")) {
//                return calUnaryExp(sons.get(1)) * -1;
//            } else {
//                return calUnaryExp(sons.get(1));
//            }
//
//        } else if (sons.get(0).getName().equals("PrimaryExp") && sons.get(0).getSys()) {
//            Node son = sons.get(0); //PrimaryExp
//            if (son.getSons().get(0).getName().equals("(")) {
//                return AddExpor.calAddExp(sons.get(0).getSons().get(1));
//            } else if (son.getSons().get(0).getName().equals("LVal") && son.getSons().get(0).getSys()) {
//                return Integer.parseInt(son.getSons().get(0).getSons().get(0).getName());
//            } else {
//                return Integer.parseInt(son.getSons().get(0).getSons().get(0).getName());
//            }
//        }
//        return 114514;
//    }

    public static boolean unaryResultIsNum(Node node) {
        ArrayList<Node> sons = node.getSons();
        if (sons.get(0).getName().equals("UnaryOp") && sons.get(0).getSys()) {
            return unaryResultIsNum(sons.get(1));
        } else if (sons.get(0).getName().equals("PrimaryExp") && sons.get(0).getSys()) {
            Node son = sons.get(0); //PrimaryExp
            if (son.getSons().get(0).getName().equals("(")) { // (Exp)
                return AddExpor.addExpResultIsNum(sons.get(0).getSons().get(1));
            } else if (son.getSons().get(0).getName().equals("LVal") && son.getSons().get(0).getSys()) { //普通变量 or 一维数组 or 二维数组
                ArrayList<Node> idents = son.getSons().get(0).getSons();
                String ident = CodeGenerator.curTable.getAlias(son.getSons().get(0).getSons().get(0).getName()); //变量名
                int dimension = 0;
                for (int k = 0; k < idents.size(); k++) { //计算数据维度
                    if (son.getSons().get(0).getSons().get(k).getName().equals("[")) {
                        dimension++;
                    }
                }
                if (dimension == 0) { //普通变量
                    if (AliasTable.isConst(Actuator.getArrayName(ident))) { //常量变量特殊处理
                        return true;
                    }
                    return false;
                } else if (dimension == 1) { // 一维数组
                    String d1 = AddExpor.calAddExpvr(son.getSons().get(0).getSons().get(2)).getName();
                    if (AliasTable.isConst(ident) && AddExpor.isPureNum(d1)) { //常量数组直接取值 : 索引也为常量
                        return true;
                    } else {
                        return false;
                    }
                } else { // 二维数组
                    String d1 = AddExpor.calAddExpvr(son.getSons().get(0).getSons().get(2)).getName();
                    String d2 = AddExpor.calAddExpvr(son.getSons().get(0).getSons().get(5)).getName();
                    if (AliasTable.isConst(ident) && AddExpor.isPureNum(d1) && AddExpor.isPureNum(d2)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {//Number
                return true;
            }
        } else {
            return false;
        }
    }

    public static TempVar calUnaryExpvr(Node node) {
        ArrayList<Node> sons = node.getSons();
        if (sons.get(0).getName().equals("UnaryOp") && sons.get(0).getSys()) {
            if (sons.get(0).getSons().get(0).getName().equals("-")) {
                if (unaryResultIsNum(sons.get(1))) { //数字直接计算
                    if (calUnaryExpvr(sons.get(1)).getName().equals("temp..0")) {
                        unaryResultIsNum(sons.get(1));
                    }
                    int numRuslt = (-1) * Integer.parseInt(calUnaryExpvr(sons.get(1)).getName());
                    TempVar result = new TempVar(Integer.toString(numRuslt));
                    return result;
                } else {
                    TempVar result = new TempVar();
                    Mul mul = new Mul(result.getName(), "-1", calUnaryExpvr(sons.get(1)).getName());
                    CodeGenerator.quaternions.add(mul);
                    return result;
                }
            } else if (sons.get(0).getSons().get(0).getName().equals("+")) { // +1 * ()
                return calUnaryExpvr(sons.get(1));
            } else {
                TempVar result = new TempVar();
                NotLogic notLogic = new NotLogic(result.getName(), calUnaryExpvr(sons.get(1)).getName());
                CodeGenerator.quaternions.add(notLogic);
                return result;
            }
        } else if (sons.get(0).getName().equals("PrimaryExp") && sons.get(0).getSys()) {
            Node son = sons.get(0); //PrimaryExp
            if (son.getSons().get(0).getName().equals("(")) { // (Exp)
                return AddExpor.calAddExpvr(sons.get(0).getSons().get(1));
            } else if (son.getSons().get(0).getName().equals("LVal") && son.getSons().get(0).getSys()) { //普通变量 or 一维数组 or 二维数组
                ArrayList<Node> idents = son.getSons().get(0).getSons();
                String ident = CodeGenerator.curTable.getAlias(son.getSons().get(0).getSons().get(0).getName()); //变量名
                int dimension = 0;
                for (int k = 0; k < idents.size(); k++) { //计算数据维度
                    if (son.getSons().get(0).getSons().get(k).getName().equals("[")) {
                        dimension++;
                    }
                }
                if (dimension == 0) { //普通变量
                    TempVar tempVar = new TempVar(ident);
                    if (AliasTable.isConst(Actuator.getArrayName(ident))) { //常量变量特殊处理
                        ArrayList<Integer> values = AliasTable.getValues(ident);
                        tempVar.setName(String.valueOf(values.get(0)));
                    }
                    return tempVar;
                } else if (dimension == 1) { // 一维数组
                    if (AliasTable.getDimension2(ident) != 0) { // 部分数组传参
                        TempVar tempVar = new TempVar();
                        String d1 = AddExpor.calAddExpvr(son.getSons().get(0).getSons().get(2)).getName();
                        GetPartOfArray getPartOfArray = new GetPartOfArray(tempVar.getName(), ident, d1);
                        CodeGenerator.quaternions.add(getPartOfArray);
                        return tempVar;
                    } else {
                        TempVar tempVar = new TempVar();
                        String d1 = AddExpor.calAddExpvr(son.getSons().get(0).getSons().get(2)).getName();
                        if (AliasTable.isConst(ident) && AddExpor.isPureNum(d1)) { //常量数组直接取值 : 索引也为常量
                            ArrayList<Integer> values = AliasTable.getValues(ident);
                            tempVar.setName(String.valueOf(values.get(Integer.parseInt(d1))));
                        } else {
                            GetValueOfArray getValueOfArray = new GetValueOfArray(tempVar.getName(), ident, d1, "", 1);
                            CodeGenerator.quaternions.add(getValueOfArray);
                        }
                        return tempVar;
                    }
                } else { // 二维数组
                    TempVar tempVar = new TempVar();
                    String d1 = AddExpor.calAddExpvr(son.getSons().get(0).getSons().get(2)).getName();
                    String d2 = AddExpor.calAddExpvr(son.getSons().get(0).getSons().get(5)).getName();
                    if (AliasTable.isConst(ident) && AddExpor.isPureNum(d1) && AddExpor.isPureNum(d2)) {
                        ArrayList<Integer> values = AliasTable.getValues(ident);
                        int index = Integer.parseInt(d1) * AliasTable.getDimension2(ident) + Integer.parseInt(d2);
                        tempVar.setName(String.valueOf(values.get(index)));
                    } else {
                        GetValueOfArray getValueOfArray = new GetValueOfArray(tempVar.getName(), ident, d1, d2, 2);
                        CodeGenerator.quaternions.add(getValueOfArray);
                    }
                    return tempVar;
                }
            } else {//Number
                TempVar tempVar = new TempVar(son.getSons().get(0).getSons().get(0).getName());
                return tempVar;
            }
        } else {//调用函数
            ArrayList<Node> sons1 = node.getSons();
            String funcName = sons1.get(0).getName();
            if (sons1.size() == 4) { //有参数
                Node funcRParams = sons1.get(2);
                int n = 0;
                ArrayList<PushParam> params = new ArrayList<>();
                for (Node exp : funcRParams.getSons()) {
                    PushParam p = new PushParam(AddExpor.calAddExpvr(exp).getName(), n);
                    p.setDimension(0);
                    Quaternion lastQuaternion;
                    if (CodeGenerator.quaternions.size() == 0) {
                        lastQuaternion = null;
                    } else {
                        lastQuaternion = CodeGenerator.quaternions.get(CodeGenerator.quaternions.size() - 1);
                    }

                    if (lastQuaternion instanceof GetPartOfArray ||
                            (!GlobalActuator.isTemp(p.getName()) && !AddExpor.isPureNum(p.getName()))) {

                        String arrayName = null;
                        if (lastQuaternion instanceof GetPartOfArray) {
                            arrayName = ((GetPartOfArray) lastQuaternion).getArrayName();
                        } else {
                            arrayName = p.getName();
                        }

                        if (AliasTable.getDimension1(arrayName) != 0) {
                            if (AliasTable.getDimension2(arrayName) == 0) {
                                p.setDimension(1);
                            } else {
                                if (lastQuaternion instanceof GetPartOfArray) {
                                    p.setPart(((GetPartOfArray) lastQuaternion).getD1());
                                    p.setName(arrayName);
                                } else {
                                    p.setDimension(2);
                                }
                            }
                        }
                    }
                    params.add(p);
                    n++;
                    CodeGenerator.quaternions.add(new Nod());
                }
                CodeGenerator.quaternions.addAll(params);
                CallFunc callFunc = new CallFunc(FuncNickTable.getNickName(funcName));
                CodeGenerator.quaternions.add(callFunc);
                TempVar tempVar = new TempVar();
                Ret ret = new Ret(tempVar.getName(), FuncNickTable.getNickName(funcName));
                CodeGenerator.quaternions.add(ret);
                return tempVar;
            } else {  //无参数
                CallFunc callFunc = new CallFunc(FuncNickTable.getNickName(funcName));
                CodeGenerator.quaternions.add(callFunc);
                TempVar tempVar = new TempVar();
                Ret ret = new Ret(tempVar.getName(), FuncNickTable.getNickName(funcName));
                CodeGenerator.quaternions.add(ret);
                return tempVar;
            }
        }
    }

}
