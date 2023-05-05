package quaternion.Label;

import Cond.CondCounter.IfCounter;
import Cond.CondCounter.WhileCounter;
import quaternion.Quaternion;

public class WhileEnd implements Quaternion, Label {
    private int no;

    public WhileEnd() {
        no = WhileCounter.getCurrentNo();
    }

    public String toString() {
        return "WhileEnd_" + no + ":";
    }

    public String getLabel() {
        return "WhileEnd_" + no;
    }
}
