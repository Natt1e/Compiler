package actuator;
import calculation.AddExpor;
import code.CodeGenerator;
import code.TempVar;
import quaternion.*;
import quaternion.Jump.JumpCode;
import quaternion.Label.Label;
import quaternion.Relation.Relation;
import table.AliasTable;
import java.util.ArrayList;


public class MainActuator {

    public static void generateMIPS(boolean isFunc) {
        for (Quaternion quaternion : CodeGenerator.quaternions) {
            if (quaternion instanceof Assign) {
                Assign assign = (Assign) quaternion;
                assignToVar(assign);
            } else if (quaternion instanceof Add) {
                Add add = (Add) quaternion;
                varBinaryExp(add, 0);
            } else if (quaternion instanceof Sub) {
                Sub sub = (Sub) quaternion;
                varBinaryExp(sub, 1);
            } else if (quaternion instanceof Mod) {
                Mod mod = (Mod) quaternion;
                varBinaryExp(mod, 4);
            } else if (quaternion instanceof Mul) {
                Mul mul = (Mul) quaternion;
                varBinaryExp(mul, 2);
            } else if (quaternion instanceof Div) {
                Div div = (Div) quaternion;
                varBinaryExp(div, 3);
            }  else if (quaternion instanceof BitAnd) {
                BitAnd bitAnd = (BitAnd) quaternion;
                varBinaryExp(bitAnd, 5);
            } else if (quaternion instanceof PrintStr) { //输出字符串
                printStr((PrintStr) quaternion);
            } else if (quaternion instanceof PrintInterger) { //输出数值
                printInteger((PrintInterger) quaternion);
            } else if (quaternion instanceof GetIn) { //输入数值
                getIn((GetIn) quaternion);
            } else if (quaternion instanceof CallFunc) {
                callFunc((CallFunc) quaternion);
            } else if (quaternion instanceof PushParam) {
                PushParam pp = (PushParam) quaternion;
                pushParam(pp);
            } else if (quaternion instanceof Return) {
                returnExp((Return) quaternion,isFunc);
            } else if (quaternion instanceof Ret) {
                getExp((Ret) quaternion);
            } else if (quaternion instanceof GetValueOfArray) {
                getValueFromArray((GetValueOfArray) quaternion);
            } else if (quaternion instanceof VarDef) {
                varDef((VarDef) quaternion);
            } else if (quaternion instanceof Relation) {
                CondActuator.workWithRelation((Relation) quaternion);
            } else if (quaternion instanceof Label) {
                Actuator.writeToMips(((Label) quaternion).getLabel() + ":");
            } else if (quaternion instanceof JumpCode) {
                CondActuator.workWithJump((JumpCode) quaternion);
            } else if (quaternion instanceof NotLogic) {
                workWithNotLogic((NotLogic) quaternion);
            } else if (quaternion instanceof GetPartOfArray) {
                Actuator.writeToMips("addi $t9 $t9 4");
            }
        }
    }

    public static void workWithNotLogic(NotLogic notLogic) {
        getExpValueToReg(notLogic.getOp1(), " $t1 ");
        Actuator.writeToMips("seq $t1 $t1 $zero");
        Actuator.writeToMips("sw $t1 " + TempVar.getNo(notLogic.getResult()) * 4 + "($fp)");
        Actuator.writeToMips("addi $t9 $t9 4");
    }

    public static void varDef(VarDef def) {
        if (def.getDimension() == 0) {
            if (def.isInitialed()) {
                getExpValueToReg(def.getValues().get(0), " $t1");
                Actuator.writeToMips("sw $t1 "+ AliasTable.getAddr(def.getVarName()) * 4 + "($gp)");
            }
            Actuator.writeToMips("addi $fp $fp 4");
        } else if (def.getDimension() == 1) {
            if (def.isInitialed()) {
                ArrayList<String> values = def.getValues();
                int address = AliasTable.getAddr(def.getVarName()) * 4;
                for (String value : values) {
                    getExpValueToReg(value, " $t1");
                    Actuator.writeToMips("sw $t1 "+ address + "($gp)");
                    address += 4;
                }
            }
            Actuator.writeToMips("addi $fp $fp " + 4 * AliasTable.getDimension1(def.getVarName()));
        } else if (def.getDimension() == 2) {
            if (def.isInitialed()) {
                ArrayList<String> values = def.getValues();
                int address = AliasTable.getAddr(def.getVarName()) * 4;
                for (String value : values) {
                    getExpValueToReg(value, " $t1");
                    Actuator.writeToMips("sw $t1 "+ address + "($gp)");
                    address += 4;
                }
            }
            Actuator.writeToMips("addi $fp $fp " + 4 * AliasTable.getDimension1(def.getVarName()) * AliasTable.getDimension2(def.getVarName()));
        }
        Actuator.writeToMips("li $t9 0");
    }

    public static void getBaseAddr(String arrayName, String reg) {      // 获得数组的基地址
        if (AliasTable.isGlobal(arrayName)) {           // 全局数组
            Actuator.writeToMips("la " + reg + " " + arrayName);
        } else if (AliasTable.getIsLocal(arrayName)) {  // 存储在当前活动记录中
            Actuator.writeToMips("addi " + reg + " $gp " + AliasTable.getAddr(arrayName) * 4);
        } else {                                        // 没有存储在当前活动记录中
            Actuator.writeToMips("addi " + reg + " $gp " + AliasTable.getAddr(arrayName) * 4);
            Actuator.writeToMips("lw " + reg +" 0(" + reg + ")");
        }
    }

    public static void getValueFromArray(GetValueOfArray gva) {
        String identName = gva.getArrayName();
        if (gva.getDimension() == 1) { // 一维数组取值
            getExpValueToReg(gva.getD1(), " $t1 "); //取索引 --> $t1
            // 取基地址 --> t2
            getBaseAddr(identName, "$t2");
            // 计算地址 --> $t1
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t2");
            //取值 --> $t1
            Actuator.writeToMips("lw $t1 0($t1)");
            // 存
            Actuator.writeToMips("sw $t1 " + TempVar.getNo(gva.getResult()) * 4 + "($fp)");

        } else { // 二维数组取值
            getExpValueToReg(gva.getD1(), " $t1 "); //取索引1 --> $t1
            getExpValueToReg(gva.getD2(), " $t2 "); //取索引2 --> $t2
            // 取基地址 --> t3
            getBaseAddr(identName, "$t3");
            // 计算地址 --> $t1
            Actuator.writeToMips("mul $t1 $t1 " + AliasTable.getDimension2(identName));
            Actuator.writeToMips("mflo $t1");
            Actuator.writeToMips("add $t1 $t1 $t2");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t3");
            // 取值 --> $t1
            Actuator.writeToMips("lw $t1 0($t1)");
            // 存
            Actuator.writeToMips("sw $t1 " + TempVar.getNo(gva.getResult()) * 4 + "($fp)");
        }
        Actuator.writeToMips("addi $t9 $t9 4");
    }

    public static void getExp(Ret ret) { //调用者接收返回值
        Actuator.writeToMips("sw $v1 " + TempVar.getNo(ret.getIdentName())*4 + "($fp)");
        Actuator.writeToMips("addi $t9 $t9 4");
    }

    public static void returnExp(Return r,boolean isFunc) { //被调用者返回值
        getExpValueToReg(r.getName(), " $v1");
        if (isFunc) {
            Actuator.writeToMips("jr $ra");
        }
    }

    public static void callFunc(CallFunc cf) { // 调用函数 与 恢复现场
        Actuator.writeToMips("# call " + cf.getName());
        Actuator.writeToMips("addi $sp $sp -12");
        Actuator.writeToMips("sw $gp 0($sp)");
        Actuator.writeToMips("sw $fp 4($sp)");
        Actuator.writeToMips("sw $ra 8($sp)");
        Actuator.writeToMips("sw $t9 0($fp)");
        Actuator.writeToMips("addi $t1 $t9 4");
        Actuator.writeToMips("add $fp $fp $t1");
        Actuator.writeToMips("move $gp $fp");

        Actuator.writeToMips("jal " + cf.getName());

        Actuator.writeToMips("lw $gp 0($sp)");
        Actuator.writeToMips("lw $fp 4($sp)");
        Actuator.writeToMips("lw $ra 8($sp)");
        Actuator.writeToMips("addi $sp $sp 12");
        Actuator.writeToMips("lw $t9 0($fp)");
    }

    public static void pushParam(PushParam pp) {
        Actuator.writeToMips("# push " + pp.getName());
        if (pp.getPart()) { //二维数组取部分 如 a[2][2] 取 a[2]
            getBaseAddr(pp.getName(), "$t0");
            getExpValueToReg(pp.getIndex(), " $t1 ");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("mul $t1 $t1 " + AliasTable.getDimension2(pp.getName()));
            Actuator.writeToMips("add $t0 $t0 $t1");
        } else {
            if (pp.getDimension() == 1) {
                getBaseAddr(pp.getName(), "$t0");
            } else if (pp.getDimension() == 2) {
                getBaseAddr(pp.getName(), "$t0");
            } else {
                getExpValueToReg(pp.getName(), " $t0");
            }
        }
        Actuator.writeToMips("move $t1 $t9");
        if (pp.getNo() != 0) {
            Actuator.writeToMips("addi $t1 $t1 " + pp.getNo()*4);
        }
        Actuator.writeToMips("add $t2 $t1 $fp");
        Actuator.writeToMips("addi $t2 $t2 4");
        Actuator.writeToMips("sw $t0 ($t2)");
    }

    public static void printStr(PrintStr p) {
        if (!p.getContent().equals("")) {
            Actuator.writeToMips("li $v0 4");
            Actuator.writeToMips("la $a0 " + p.getStrName());
            Actuator.writeToMips("syscall");
        }
    }

    public static void getIn(GetIn gt) {        // 仿照赋值
        Actuator.writeToMips("li $v0 5");
        Actuator.writeToMips("syscall");

        String identName = gt.getName();
        if (gt.getDimension() == 0) {       // 为普通变量赋值
            if (AliasTable.isGlobal(identName)) {
                Actuator.writeToMips("sw $v0 " + identName);
            } else {
                Actuator.writeToMips("sw $v0 " + AliasTable.getAddr(identName) * 4 + "($gp)");
            }
        } else if (gt.getDimension() == 1) { // 为一维数组赋值
            getExpValueToReg(gt.getD1(), " $t1 ");   //索引值
            getBaseAddr(identName, "$t3");  //取基地址
            // 算偏移
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t3");
            Actuator.writeToMips("sw $v0 0($t1)");
        } else {                                // 为二维数组赋值
            getExpValueToReg(gt.getD1(), " $t1 ");   //索引值1
            getExpValueToReg(gt.getD2(), " $t2 ");   //索引值2
            getBaseAddr(identName, "$t4"); //取基地址
            // 算偏移
            Actuator.writeToMips("mul $t1 $t1 " + AliasTable.getDimension2(identName));
            Actuator.writeToMips("mflo $t1");
            Actuator.writeToMips("add $t1 $t1 $t2");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t4");
            Actuator.writeToMips("sw $v0 0($t1)");
        }
    }

    public static void printInteger(PrintInterger p) {
        getExpValueToReg(p.getName(), " $a0 ");
        Actuator.writeToMips("li $v0 1");
        Actuator.writeToMips("syscall");
    }

    public static void assignToVar(Assign assign) {
        String identName = assign.getIdentName();
        if (assign.getDimension() == 0) {       // 为普通变量赋值
            getExpValueToReg(assign.getAgent(), " $t1 ");  //将待存的值存入 $t1 寄存器
            // 将 $t1 存入
            if (AliasTable.isGlobal(assign.getIdentName())) {
                Actuator.writeToMips("sw $t1 " + identName);
            } else {
                Actuator.writeToMips("sw $t1 " + AliasTable.getAddr(identName) * 4 + "($gp)");
            }
        } else if (assign.getDimension() == 1) { // 为一维数组赋值
            getExpValueToReg(assign.getD1(), " $t1 ");   //索引值
            getExpValueToReg(assign.getAgent(), " $t2 "); //待存值
            //取基地址 --> t3
            getBaseAddr(identName, "$t3");
            // 算偏移
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t3");
            Actuator.writeToMips("sw $t2 0($t1)");
        } else {                                // 为二维数组赋值
            getExpValueToReg(assign.getD1(), " $t1 ");   //索引值1
            getExpValueToReg(assign.getD2(), " $t2 ");   //索引值2
            getExpValueToReg(assign.getAgent(), " $t3 "); //待存值
            // 取基地址-->t4
            getBaseAddr(identName, "$t4");
            // 算偏移
            Actuator.writeToMips("mul $t1 $t1 " + AliasTable.getDimension2(identName));
            Actuator.writeToMips("mflo $t1");
            Actuator.writeToMips("add $t1 $t1 $t2");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t1");
            Actuator.writeToMips("add $t1 $t1 $t4");
            Actuator.writeToMips("sw $t3 0($t1)");
        }
    }

    public static void getExpValueToReg(String exp,String reg) {
        /*
        分为三种情况: 1.纯数字
                    2.临时变量
                    3.普通变量(指非数组)
         */
        if (AddExpor.isPureNum(exp)) {              // 纯数字
            Actuator.writeToMips("li" + reg + " " + exp);
        } else if (GlobalActuator.isTemp(exp)) {    //临时变量
            Actuator.writeToMips("lw" + reg + " " + TempVar.getNo(exp) * 4 + "($fp)");
        } else {                                    //变量
            if (AliasTable.isGlobal(exp)) {         //全局变量
                Actuator.writeToMips("lw" + reg + " " + exp);
            } else {                                //局部变量
                Actuator.writeToMips("lw" + reg + " " + AliasTable.getAddr(exp) * 4 + "($gp)");
            }
        }
    }

    public static void varBinaryExp(BinaryExp exp, int type) {
        getExpValueToReg(exp.getOp1(), " $t1 ");
        getExpValueToReg(exp.getOp2(), " $t2 ");
        switch (type) {
            case 0:
                Actuator.writeToMips("add $t1 $t1 $t2");
                break;//加
            case 1:
                Actuator.writeToMips("sub $t1 $t1 $t2");
                break;//减
            case 2:
                Actuator.writeToMips("mul $t1 $t1 $t2");
                break;    //乘
            case 5:
                Actuator.writeToMips("and $t1 $t1 $t2");
                break;
            default:
                Actuator.writeToMips("div $t1 $t2");
        }
        if (type == 3) {
            Actuator.writeToMips("mflo $t1");
        } else if (type == 4) {
            Actuator.writeToMips("mfhi $t1");
        }
        Actuator.writeToMips("sw $t1 " + TempVar.getNo(exp.getResult()) * 4 + "($fp)");
        Actuator.writeToMips("addi $t9 $t9 4");
    }


}
