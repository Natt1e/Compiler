package quaternion;

public class NotLogic implements Quaternion {
    private final String result;
    private final String op1;

    public NotLogic(String result,String op1) {
        this.result = result;
        this.op1 = op1;
    }

    public String getResult() {
        return this.result;
    }

    public String getOp1() {
        return this.op1;
    }

    public String toString() {
        return this.result + " = ! " + op1;
    }
}
