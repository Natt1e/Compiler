package quaternion.Relation;

import quaternion.Quaternion;

public class Equal implements Quaternion, Relation{
    private String op1;
    private String op2;
    private String result;

    public Equal(String result, String op1, String op2) {
        this.result = result;
        this.op1 = op1;
        this.op2 = op2;
    }

    public String getResult() {
        return this.result;
    }

    public String getOp1() {
        return this.op1;
    }

    public String getOp2() {
        return this.op2;
    }

    public String toString() {
        return this.result + " = ? (" + op1 + " == " + op2 + ")";
    }
}
