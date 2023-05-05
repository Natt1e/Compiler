package quaternion;

public class Ret implements Quaternion {
    private String identName;
    private String funcName;

    public Ret(String identName,String funcName) {
        this.identName = identName;
        this.funcName = funcName;
    }

    public String toString() {
        return identName + " get " + funcName + "()";
    }

    public String getIdentName() {
        return this.identName;
    }

}
