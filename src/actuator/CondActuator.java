package actuator;

import code.TempVar;
import quaternion.Jump.Jump;
import quaternion.Jump.JumpCode;
import quaternion.Jump.JumpWhenNotZero;
import quaternion.Jump.JumpWhenZero;
import quaternion.Relation.*;

public class CondActuator {

    public static void workWithRelation(Relation relation) {
        String op1 = relation.getOp1();
        String op2 = relation.getOp2();
        MainActuator.getExpValueToReg(op1, " $t1 ");
        MainActuator.getExpValueToReg(op2, " $t2 ");
        if (relation instanceof Equal) {
            Actuator.writeToMips("seq $t3 $t1 $t2");
        } else if (relation instanceof Greater) {
            Actuator.writeToMips("sgt $t3 $t1 $t2");
        } else if (relation instanceof GreaterOrEq) {
            Actuator.writeToMips("sge $t3 $t1 $t2");
        } else if (relation instanceof Less) {
            Actuator.writeToMips("slt $t3 $t1 $t2");
        } else if (relation instanceof LessOrEq) {
            Actuator.writeToMips("sle $t3 $t1 $t2");
        } else if (relation instanceof NotEqual) {
            Actuator.writeToMips("sne $t3 $t1 $t2");
        }
        Actuator.writeToMips("sw $t3 " + TempVar.getNo(relation.getResult())*4 + "($fp)");
    }

    public static void workWithJump(JumpCode jumpCode) {
        String label = jumpCode.getLabel();
        if (jumpCode instanceof Jump) {
            Actuator.writeToMips("j " + label);
        } else if (jumpCode instanceof JumpWhenZero) {
            JumpWhenZero j = (JumpWhenZero) jumpCode;
            MainActuator.getExpValueToReg(j.getVar(), " $t1 ");
            Actuator.writeToMips("beqz $t1 " + label);
        } else if (jumpCode instanceof JumpWhenNotZero) {
            JumpWhenNotZero j = (JumpWhenNotZero) jumpCode;
            MainActuator.getExpValueToReg(j.getVar(), " $t1 ");
            Actuator.writeToMips("bnez $t1 " + label);
        }
    }


}
