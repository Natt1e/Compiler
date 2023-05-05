package Cond;

import calculation.AddExpor;
import code.CodeGenerator;
import code.TempVar;
import parse.Node;
import quaternion.End;
import quaternion.Quaternion;
import quaternion.Relation.Greater;
import quaternion.Relation.GreaterOrEq;
import quaternion.Relation.Less;
import quaternion.Relation.LessOrEq;

import java.util.ArrayList;

public class RelExpHandler {

    public static String handleRelExp(Node relExp) {
        ArrayList<Node> sons = relExp.getSons();
        int flag = 0;
        int size = sons.size();
        String op1 = AddExpor.calAddExpvr(sons.get(0)).getName();
        for (int i = 1; i < size;i++) {
            String identifier = sons.get(i).getName();
            if (identifier.equals("<")) {
                flag = 1;
            } else if (identifier.equals("<=")) {
                flag = 2;
            } else if (identifier.equals(">")) {
                flag = 3;
            } else if (identifier.equals(">=")) {
                flag = 4;
            } else {
                TempVar tempVar = new TempVar();
                String op2 = AddExpor.calAddExpvr(sons.get(i)).getName();
                if (flag == 1) {
                    Less less = new Less(tempVar.getName(), op1, op2);
                    CodeGenerator.quaternions.add(less);
                } else if (flag == 2) {
                    LessOrEq lessOrEq = new LessOrEq(tempVar.getName(), op1, op2);
                    CodeGenerator.quaternions.add(lessOrEq);
                } else if (flag == 3) {
                    Greater greater = new Greater(tempVar.getName(), op1, op2);
                    CodeGenerator.quaternions.add(greater);
                } else {
                    GreaterOrEq greaterOrEq = new GreaterOrEq(tempVar.getName(), op1, op2);
                    CodeGenerator.quaternions.add(greaterOrEq);
                }
                op1 = tempVar.getName();
            }
        }
        return op1;
    }

}
