package quaternion;

public class LoadParam implements Quaternion{
    private String name;

    public LoadParam(String name) {
        this.name = name;
    }

    public String toString() {
        return "load " + this.name;
    }

}
