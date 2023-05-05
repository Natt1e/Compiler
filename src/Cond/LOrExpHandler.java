package Cond;

import code.CodeGenerator;
import parse.Node;
import quaternion.End;
import quaternion.Jump.Jump;
import quaternion.Label.IfBegin;

import java.util.ArrayList;

public class LOrExpHandler {

    public static void HandleLOrExp(Node lOr, String beginLabel, String endLabel, boolean isIf) {
        ArrayList<Node> sons = lOr.getSons();

        for (Node son : sons) {
            if (!son.getName().equals("||")) {
                LAndExpHandler.handleLAndExp(son, beginLabel, isIf);
            }
        }

        Jump jump = new Jump(endLabel);
        CodeGenerator.quaternions.add(jump);
        End end = new End();
        CodeGenerator.quaternions.add(end);
    }

}
