package code;

import actuator.Actuator;
import actuator.GlobalActuator;
import calculation.AddExpor;
import parse.Node;
import quaternion.Assign;
import quaternion.VarDef;
import table.AliasTable;

import java.util.ArrayList;
/*
生成全局变量的中间代码
 */
public class GlobalCodeGenerator {

    public static void processGlobal(Node node) { //处理全局变量
        ArrayList<Node> sons = node.getSons();
        for (Node son : sons) {
            if (son.getName().equals("Decl")) {
                if (son.getSons().get(0).getName().equals("ConstDecl")) {
                    int i = 2;
                    while (son.getSons().get(0).getSons().get(i).getName().equals("ConstDef")) {
                        Node constDef = son.getSons().get(0).getSons().get(i);
                        ConstDefCode(constDef);
                        i++;
                    }
                } else {
                    int i = 1;
                    while (son.getSons().get(0).getSons().get(i).getName().equals("VarDef")) {
                        Node varDef = son.getSons().get(0).getSons().get(i);
                        VarDefCode(varDef);
                        i++;
                    }

                }
            }
        }
        //Actuator.generateVarDataArea();//生成data区的变量全局变量
        GlobalActuator.globalVarMain();
    }


    public static void ConstDefCode(Node node) {
        ArrayList<Node> sons = node.getSons();
        String identName = sons.get(0).getName();
        int dimension = 0; //维度
        int dimensionSum1;
        int dimensionSum2;
        for (Node son : sons) {
            if (son.getName().equals("[")) {
                dimension++;
            }
        }
        if (dimension == 0) {   //非数组，即普通变量
            addVarToTable(identName,true,0,0,true);
            AliasTable.addValue(CodeGenerator.curTable.getAlias(identName),Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName()));
            VarDef def = new VarDef(CodeGenerator.curTable.getAlias(identName), 0);
            CodeGenerator.quaternions.add(def);
        } else if (dimension == 1) { //一维数组
            dimensionSum1 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName()); // 数组元素个数
            addVarToTable(identName,true, dimensionSum1,0,true);
            Node nums = sons.get(5);
            VarDef def = new VarDef(CodeGenerator.curTable.getAlias(identName), 1);
            CodeGenerator.quaternions.add(def);
            for (int j = 0; j < dimensionSum1; j++) {
                AliasTable.addValue(CodeGenerator.curTable.getAlias(identName), Integer.parseInt(AddExpor.calAddExpvr(nums.getSons().get(j + 1)).getName()));
            }
        } else if (dimension == 2) { // 二维数组
            dimensionSum1 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName()); // 行数
            dimensionSum2 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(5)).getName());// 列数
            addVarToTable(identName,true, dimensionSum1, dimensionSum2,true);
            Node nums = sons.get(8);
            VarDef def = new VarDef(CodeGenerator.curTable.getAlias(identName), 2);
            CodeGenerator.quaternions.add(def);
            for (int i = 0; i < dimensionSum1; i++) {
                Node group = nums.getSons().get(i + 1);
                for (int j = 0; j < dimensionSum2; j++) {
                    AliasTable.addValue(CodeGenerator.curTable.getAlias(identName),Integer.parseInt(AddExpor.calAddExpvr(group.getSons().get(j + 1)).getName()));
                }
            }
        }
    }

    public static void VarDefCode(Node node) {
        ArrayList<Node> sons = node.getSons();
        String identName = sons.get(0).getName();
        int dimension = 0; //维度
        int dimensionSum1;
        int dimensionSum2;
        for (Node son : sons) {
            if (son.getName().equals("[")) {
                dimension++;
            }
        }
        if (dimension == 0) {   //非数组，即普通变量
            addVarToTable(identName,false,0,0,true);
            VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 0);
            if (sons.size() == 3) {
                TempVar t = AddExpor.calAddExpvr(sons.get(2));
                vd.addValue(t.getName());
            }
            CodeGenerator.quaternions.add(vd);
        } else if (dimension == 1) { //一维数组
            dimensionSum1 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName()); // 数组元素个数
            addVarToTable(identName,false,dimensionSum1,0,true);
            VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 1);
            if (sons.size() == 6) {
                Node nums = sons.get(5);
                for (int j = 0; j < dimensionSum1; j++) {
                    TempVar t = AddExpor.calAddExpvr(nums.getSons().get(j + 1));
                    vd.addValue(t.getName());
                    //Assign assign = new Assign(CodeGenerator.curTable.getAlias(identName),t.getName(),j);
                    //CodeGenerator.quaternions.add(assign);
                }
            } else {
                for (int j = 0; j < dimensionSum1; j++) {
                    //Assign assign = new Assign(CodeGenerator.curTable.getAlias(identName),"0",j);
                    //CodeGenerator.quaternions.add(assign);
                    vd.addValue("0");
                }
            }
            CodeGenerator.quaternions.add(vd);
        } else if (dimension == 2) { // 二维数组
            dimensionSum1 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(2)).getName()); // 行数
            dimensionSum2 = Integer.parseInt(AddExpor.calAddExpvr(sons.get(5)).getName());// 列数
            addVarToTable(identName,false,dimensionSum1,dimensionSum2,true);
            VarDef vd = new VarDef(CodeGenerator.curTable.getAlias(identName), 2);
            if (sons.size() == 9) {
                Node nums = sons.get(8);
                for (int i = 0; i < dimensionSum1; i++) {
                    Node group = nums.getSons().get(i + 1);
                    for (int j = 0; j < dimensionSum2; j++) {
                        TempVar t = AddExpor.calAddExpvr(group.getSons().get(j + 1));
                        //Assign assign = new Assign(CodeGenerator.curTable.getAlias(identName),t.getName(),i * dimensionSum2 + j);
                        //CodeGenerator.quaternions.add(assign);
                        vd.addValue(t.getName());
                    }
                }
            } else {
                for (int i = 0; i < dimensionSum1; i++) {
                    for (int j = 0; j < dimensionSum2; j++) {
                        //Assign assign = new Assign(CodeGenerator.curTable.getAlias(identName),"0",i * dimensionSum2 + j);
                        //CodeGenerator.quaternions.add(assign);
                        vd.addValue("0");
                    }
                }
            }
            CodeGenerator.quaternions.add(vd);
        }
    }

    public static void addVarToTable(String identName,boolean isConst,int d1,int d2,boolean isGlobal) {
        /*
        完成了两步工作:
        1.将变量加入到符号表(已存addr)
        2.将变量加入到别名符号表 (已存addr)
         */
        if (d1 == 0 && d2 == 0) {
            CodeGenerator.curTable.addSymbol(identName, 0, isConst);
        } else if (d1 > 0 && d2 == 0) {
            CodeGenerator.curTable.addSymbol(identName, 1, isConst);
        } else {
            CodeGenerator.curTable.addSymbol(identName, -1, isConst);
        }
        String alias = VarNamer.getAlias(identName);
        CodeGenerator.curTable.setAlias(identName,alias);
        CodeGenerator.aliasTable.addAlias(alias,isConst,CodeGenerator.addr,d1,d2,isGlobal);
    }

}
