package quaternion;

public class Assign implements Quaternion {
    private final String identName;
    private final int dimension;
    private final String agent;
    private String d1;
    private String d2;

    public Assign(String identName, String agent, String d1, String d2, int dimension) {
        this.identName = identName;
        this.dimension = dimension;
        this.agent = agent;
        this.d1 = d1;
        this.d2 = d2;
    }

    public int getDimension() {
        return this.dimension;
    }

    public String getAgent() {
        return this.agent;
    }

    public String getD1() {
        return this.d1;
    }

    public String getD2() {
        return this.d2;
    }

    public String getIdentName() {
        return this.identName;
    }

    public String toString() {
        return identName + " get " + agent;
    }

}
