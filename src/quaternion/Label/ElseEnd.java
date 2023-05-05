package quaternion.Label;

import Cond.CondCounter.IfCounter;
import quaternion.Quaternion;

public class ElseEnd implements Quaternion, Label {
    private int no;

    public ElseEnd() {
        no = IfCounter.getCurrentNo();
    }

    public String toString() {
        return "ElseEnd_" + no + ":";
    }

    public String getLabel() {
        return "ElseEnd_" + no;
    }
}
