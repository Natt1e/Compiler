package actuator;

import code.CodeGenerator;
import quaternion.End;
import quaternion.LoadParam;
import quaternion.Quaternion;

public class FuncActuator {
    public static void processFuncBlock() {
        int paramSum = 0; //统计参数数量
        CodeGenerator.quaternions.add(new End());
        for (Quaternion quaternion : CodeGenerator.quaternions) {
            if (quaternion instanceof LoadParam) {
                paramSum++;
            }
        }
        if (paramSum != 0) {
            Actuator.writeToMips("addi $fp $fp " + 4 * paramSum);
        }
        MainActuator.generateMIPS(true);
//        for (Quaternion quaternion : CodeGenerator.quaternions) {
//            System.out.println(quaternion.toString());
//        }
        CodeGenerator.quaternions.clear();
        Actuator.writeToMips("jr $ra");
        Actuator.writeToMips("");
    }
}
