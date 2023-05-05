package Cond;

import code.CodeGenerator;
import parse.Node;
import quaternion.Jump.Jump;
import quaternion.Jump.JumpWhenZero;
import quaternion.Label.LAndEnd;

import java.util.ArrayList;

public class LAndExpHandler {

    public static void handleLAndExp(Node lAndExp, String label, boolean isIf) {
        ArrayList<Node> sons = lAndExp.getSons();

        LAndEnd lAndEnd = new LAndEnd(isIf);

        for (Node son : sons) {
            if (!son.getName().equals("&&")) {
                String result = EqExpHandler.handleEqExp(son);
                JumpWhenZero jumpWhenZero = new JumpWhenZero(result, lAndEnd.getLabel());
                CodeGenerator.quaternions.add(jumpWhenZero);
            }
        }

        Jump jump = new Jump(label);
        CodeGenerator.quaternions.add(jump);
        CodeGenerator.quaternions.add(lAndEnd);
    }

}
