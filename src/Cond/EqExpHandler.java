package Cond;

import code.CodeGenerator;
import code.TempVar;
import parse.Node;
import quaternion.Relation.Equal;
import quaternion.Relation.NotEqual;

import java.util.ArrayList;

public class EqExpHandler {

    public static String handleEqExp(Node eqExp) {
        ArrayList<Node> sons = eqExp.getSons();
        String op1 = RelExpHandler.handleRelExp(sons.get(0));
        int flag = 0;
        int size = sons.size();
        for (int i = 1;i < size;i++) {
            String identifier = sons.get(i).getName();
            if (identifier.equals("==")) {
                flag = 1;
            } else if (identifier.equals("!=")) {
                flag = 2;
            } else {
                TempVar tempVar = new TempVar();
                String op2 = RelExpHandler.handleRelExp(sons.get(i));
                if (flag == 1) {
                    Equal equal = new Equal(tempVar.getName(), op1, op2);
                    CodeGenerator.quaternions.add(equal);
                } else {
                    NotEqual notEqual = new NotEqual(tempVar.getName(), op1, op2);
                    CodeGenerator.quaternions.add(notEqual);
                }
                op1 = tempVar.getName();
            }
        }
        return op1;
    }

}
