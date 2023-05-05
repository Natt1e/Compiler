package quaternion;

public class GetPartOfArray implements Quaternion {
    private final String result;
    private final String arrayName;
    private final String d1;

    public GetPartOfArray(String result,String arrayName,String d1) {
        this.result = result;
        this.arrayName = arrayName;
        this.d1 = d1;
    }

    public String getResult() {
        return this.result;
    }

    public String getArrayName() {
        return this.arrayName;
    }

    public String getD1() {
        return this.d1;
    }



    public String toString() {
        return this.result + " := " + arrayName + "[" + d1 + "]";
    }
}
