package quaternion.Label;

import Cond.CondCounter.IfCounter;
import Cond.CondCounter.WhileCounter;
import quaternion.Quaternion;

public class WhileBegin implements Quaternion, Label {
    private int no;

    public WhileBegin() {
        no = WhileCounter.getCurrentNo();
    }

    public String toString() {
        return "WhileBegin_" + no + ":";
    }

    public String getLabel() {
        return "WhileBegin_" + no;
    }
}
