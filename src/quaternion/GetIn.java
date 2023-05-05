package quaternion;

public class GetIn implements Quaternion {
    private String name;
    private String d1;
    private String d2;
    private int dimension; // 0 or 1 or 2

    public GetIn(String name, int dimension, String d1, String d2) {
        this.name = name;
        this.dimension = dimension;
        this.d1 = d1;
        this.d2 = d2;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name + " : getin()";
    }

    public int getDimension() {
        return this.dimension;
    }

    public String getD1() {
        return this.d1;
    }

    public String getD2() {
        return this.d2;
    }

}
