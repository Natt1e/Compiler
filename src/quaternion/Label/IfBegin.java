package quaternion.Label;

import Cond.CondCounter.IfCounter;
import quaternion.Quaternion;

public class IfBegin implements Quaternion, Label {
    private int no;

    public IfBegin() {
        no = IfCounter.getCurrentNo();
    }

    public String toString() {
        return "IfBegin_" + no + ":";
    }

    public String getLabel() {
        return "IfBegin_" + no;
    }

}
