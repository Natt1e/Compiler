package quaternion.Label;

import Cond.CondCounter.IfCounter;
import Cond.CondCounter.WhileCounter;
import quaternion.Quaternion;

public class WhileBody implements Quaternion, Label {
    private int no;

    public WhileBody() {
        no = WhileCounter.getCurrentNo();
    }

    public String toString() {
        return "WhileBody_" + no + ":";
    }

    public String getLabel() {
        return "WhileBody_" + no;
    }
}
