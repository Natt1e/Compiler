package quaternion;

public class CallFunc implements Quaternion {
    private String funcName;

    public CallFunc(String funcName) {
        this.funcName = funcName;
    }

    public String toString() {
        return "call " + funcName;
    }

    public String getName() {
        return this.funcName;
    }

}
