package quaternion.Label;

import Cond.CondCounter.IfCounter;
import Cond.CondCounter.LAndCounter;
import Cond.CondCounter.WhileCounter;
import quaternion.Quaternion;

public class LAndEnd implements Quaternion, Label {
    private int no;
    private boolean isIf;
    private int ifNo;
    private int whileNo;

    public LAndEnd(boolean isIf) {
        this.no = LAndCounter.getNewNo();
        this.isIf = isIf;
        this.ifNo = IfCounter.getCurrentNo();
        this.whileNo = WhileCounter.getCurrentNo();
    }

    public String toString() {
        if (isIf) {
            return "If_" + ifNo + "_LAnd_END_" + no + ":";
        }
        return "While_" + whileNo + "_LAnd_END_" + no + ":";
    }

    public String getLabel() {
        if (isIf) {
            return "If_" + ifNo + "_LAnd_END_" + no;
        }
        return "While_" + whileNo + "_LAnd_END_" + no;
    }

}
