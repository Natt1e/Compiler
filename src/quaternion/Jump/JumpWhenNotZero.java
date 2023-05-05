package quaternion.Jump;

import quaternion.Quaternion;

public class JumpWhenNotZero implements Quaternion, JumpCode {
    private String var;
    private String label;

    public JumpWhenNotZero(String var, String label) {
        this.label = label;
        this.var = var;
    }

    public String toString() {
        return "(" + var + " == 0) ? jump to " + label;
    }

    public String getVar() {
        return this.var;
    }

    public String getLabel() {
        return this.label;
    }
}
