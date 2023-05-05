package actuator;

import calculation.AddExpor;
import code.CodeGenerator;
import quaternion.*;
import table.AliasTable;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalActuator {
    private static HashMap<String, Integer> tempValues;

    public static void globalVarMain() {
        tempValues = new HashMap<>();
        ArrayList<Quaternion> quaternions = CodeGenerator.quaternions;
        for (Quaternion q : quaternions) {
            if (q instanceof VarDef) {
                VarDef v = (VarDef) q;

                if (AliasTable.isConst(v.getVarName())) { //常量变量声明
                    printConstDataText(v);
                } else { //变量声明
                    printVarDataText(v);
                }

            } else if (q instanceof Add) {
                Add add = (Add) q;
                tempValues.put(add.getResult(), getConcreteValue(add.getOp1()) + getConcreteValue(add.getOp2()));
            } else if (q instanceof Sub) {
                Sub sub = (Sub) q;
                tempValues.put(sub.getResult(), getConcreteValue(sub.getOp1()) - getConcreteValue(sub.getOp2()));
            } else if (q instanceof Mul) {
                Mul mul = (Mul) q;
                tempValues.put(mul.getResult(), getConcreteValue(mul.getOp1()) * getConcreteValue(mul.getOp2()));
            } else if (q instanceof Div) {
                Div div = (Div) q;
                tempValues.put(div.getResult(), getConcreteValue(div.getOp1()) / getConcreteValue(div.getOp2()));
            } else if (q instanceof GetValueOfArray) {
                GetValueOfArray goa = (GetValueOfArray) q;
                tempValues.put(goa.getResult(),getValueFromArray(goa));
            } else if (q instanceof BitAnd) {
                BitAnd bitAnd = (BitAnd) q;
                tempValues.put(bitAnd.getResult(), getConcreteValue(bitAnd.getOp1()) & getConcreteValue(bitAnd.getOp2()));
            } else if (q instanceof Mod) {
                Mod mod = (Mod) q;
                tempValues.put(mod.getResult(), getConcreteValue(mod.getOp1()) % getConcreteValue(mod.getOp2()));
            }
        }
        CodeGenerator.quaternions.clear();
    }

    public static int getConcreteValue(String name) {
        if (AddExpor.isPureNum(name)) {     // 数字直接返回
            return Integer.parseInt(name);
        } else if (isTemp(name)) {          // 临时变量查哈希表
            return tempValues.get(name);
        } else {                            // 变量查别名表
            return AliasTable.getValues(name).get(0);
        }
    }

    public static int getValueFromArray(GetValueOfArray goa) {
        ArrayList<Integer> values = AliasTable.getValues(goa.getArrayName());
        if (goa.getDimension() == 1) {      // 一维数组取值
            int d1 = getConcreteValue(goa.getD1());
            return values.get(d1);
        } else {                            // 二维数组取值
            int d1 = getConcreteValue(goa.getD1());
            int d2 = getConcreteValue(goa.getD2());
            return values.get(d1*AliasTable.getDimension2(goa.getArrayName()) + d2);
        }
    }

    public static void printConstDataText(VarDef v) {
        ArrayList<Integer> values = AliasTable.getValues(v.getVarName());
        StringBuilder content = new StringBuilder(v.getVarName() + ": .word " + values.get(0));
        for (int i = 1; i < values.size(); i++) {
            content.append(", ").append(values.get(i));
        }
        Actuator.writeToMips(content.toString());
    }

    public static void printVarDataText(VarDef v) {
        ArrayList<String> values = v.getValues();
        StringBuilder content = new StringBuilder(v.getVarName() + " : .word ");
        if (values.size() == 0) {   //没有初始值，赋0
            content.append("0");
            AliasTable.addValue(v.getVarName(), 0);
            if (v.getDimension() == 1) {   // 一维数组
                for (int i = 1; i < AliasTable.getDimension1(v.getVarName()); i++) {
                    content.append(", ").append("0");
                    AliasTable.addValue(v.getVarName(), 0);
                }
            } else {  //二维数组
                for (int i = 1; i < AliasTable.getDimension1(v.getVarName()) * AliasTable.getDimension2(v.getVarName()); i++) {
                    content.append(", ").append("0");
                    AliasTable.addValue(v.getVarName(), 0);
                }
            }
        } else {   //有初始值，进行赋值
            if (AddExpor.isPureNum(values.get(0))) {  //数字直接赋值
                content.append(values.get(0));
                AliasTable.addValue(v.getVarName(), Integer.parseInt(values.get(0)));
            } else if (isTemp(values.get(0))) {     //临时变量为其赋值，查哈希表
                content.append(tempValues.get(values.get(0)));
                AliasTable.addValue(v.getVarName(), tempValues.get(values.get(0)));
            } else {                                 //全局变量为其赋值，查别名表
                ArrayList<Integer> varValues = AliasTable.getValues(values.get(0));
                content.append(varValues.get(0));
                AliasTable.addValue(v.getVarName(), varValues.get(0));
            }
            if (v.getDimension() == 1) {      //一维数组
                for (int i = 1; i < AliasTable.getDimension1(v.getVarName()); i++) {
                    addVarValue(v.getVarName(), values.get(i), content);
                }
            } else {                    //二维数组
                for (int i = 1;i < AliasTable.getDimension1(v.getVarName())*AliasTable.getDimension2(v.getVarName()); i++) {
                    addVarValue(v.getVarName(), values.get(i), content);
                }
            }
        }
        Actuator.writeToMips(content.toString());
        tempValues.clear();
    }

    public static void addVarValue(String name, String value, StringBuilder content) {
        if (AddExpor.isPureNum(value)) {  //数字直接赋值
            content.append(", ").append(value);
            AliasTable.addValue(name, Integer.parseInt(value));
        } else if (isTemp(value)) {     //临时变量为其赋值，查哈希表
            content.append(", ").append(tempValues.get(value));
            AliasTable.addValue(name, tempValues.get(value));
        } else {                         //全局变量为其赋值，查别名表
            ArrayList<Integer> varValues = AliasTable.getValues(value);
            content.append(", ").append(varValues.get(0));
            AliasTable.addValue(name, varValues.get(0));
        }
    }


    public static boolean isTemp(String name) {
        return name.indexOf("temp..") == 0;
    }


}
