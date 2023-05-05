package quaternion.Jump;

import quaternion.Quaternion;

public class Jump implements Quaternion, JumpCode {

    private String label;

    public Jump(String label) {
        this.label = label;
    }

    public String toString() {
        return "jump to " + label;
    }

    public String getLabel() {
        return this.label;
    }

}
