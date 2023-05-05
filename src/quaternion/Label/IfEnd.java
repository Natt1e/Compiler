package quaternion.Label;

import Cond.CondCounter.IfCounter;
import quaternion.Quaternion;

public class IfEnd implements Quaternion, Label {
    private int no;

    public IfEnd() {
        no = IfCounter.getCurrentNo();
    }

    public String toString() {
        return "IfEnd_" + no + ":";
    }

    public String getLabel() {
        return "IfEnd_" + no ;
    }
}
