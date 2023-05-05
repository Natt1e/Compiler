package quaternion;

public class GetValueOfArray implements Quaternion {
    private final String result;
    private final String arrayName;
    private final String d1;
    private final String d2;
    private final int dimension;

    public GetValueOfArray(String result,String arrayName,String d1,String d2,int dimension) {
        this.result = result;
        this.arrayName = arrayName;
        this.d1 = d1;
        this.d2 = d2;
        this.dimension = dimension;
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

    public String getD2() {
        return this.d2;
    }

    public int getDimension() {
        return this.dimension;
    }

    public String toString() {
        if (dimension == 1) { //一维数组取值
            return this.result + " := " + arrayName + "[" + d1 + "]";
        } else { //二维数组取值
            return this.result + " := " + arrayName + "[" + d1 + "]" + "[" + d2 + "]";
        }
    }

}
