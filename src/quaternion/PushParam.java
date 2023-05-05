package quaternion;

public class PushParam implements Quaternion {
    private String paramName;
    private int no;
    private int dimension;
    private boolean part;
    private String index;

    public PushParam(String name,int no) {
        this.paramName = name;
        this.no = no;
        this.part = false;
    }

    public void setPart(String index) {
        this.part = true;
        this.index = index;
    }

    public boolean getPart() {
        return this.part;
    }

    public String getIndex() {
        return this.index;
    }

    public String getName() {
        return this.paramName;
    }

    public String toString() {
        return "push " + this.paramName;
    }

    public int getNo() {
        return this.no;
    }

    public void setDimension(int d) {
        this.dimension = d;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setName(String name) {
        this.paramName = name;
    }

}
