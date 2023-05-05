package code;

import actuator.Actuator;
import actuator.FuncActuator;
import calculation.AddExpor;
import parse.Node;
import quaternion.LoadParam;
import table.AliasTable;
import table.FuncNickCounter;
import table.FuncNickTable;

import java.util.ArrayList;

public class FuncGenerator {

    public static void generateFuncBlock(Node funcDef) {
        boolean isReturn = true;
        ArrayList<Node> sons = funcDef.getSons();
        if (sons.get(0).getName().equals("void")) {
            isReturn = false; // void类型 没有返回值
        }
        String nickName = FuncNickCounter.getNickName(sons.get(1).getName());
        FuncNickTable.addFunc(sons.get(1).getName(),isReturn);
        Actuator.writeToMips(nickName+":"); //函数体的开始
        if (sons.get(2).getName().equals("FuncFParams")) { //处理形参
            for (Node fparam : sons.get(2).getSons()) {
                String paramName = fparam.getSons().get(1).getName();
                if (fparam.getSons().size() == 2) {        // 传普通变量
                    GlobalCodeGenerator.addVarToTable(paramName,false,0,0,false);
                } else if (fparam.getSons().size() == 4) { // 传一维数组
                    GlobalCodeGenerator.addVarToTable(paramName, false, 1, 0, false);
                    AliasTable.setIsLocal(CodeGenerator.curTable.getAlias(paramName), false);
                } else {                                   // 传二维数组
                    int d2 = Integer.parseInt(AddExpor.calAddExpvr(fparam.getSons().get(5)).getName());
                    GlobalCodeGenerator.addVarToTable(paramName, false, 1, d2, false);
                    AliasTable.setIsLocal(CodeGenerator.curTable.getAlias(paramName), false);
                }
                CodeGenerator.addr++;
                LoadParam lp = new LoadParam(CodeGenerator.curTable.getAlias(paramName));
                CodeGenerator.quaternions.add(lp);
            }
        }
        CodeGenerator.ergodicMainTree(sons.get(3));
        FuncActuator.processFuncBlock();
    }

}
