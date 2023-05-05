package quaternion;

public class Return implements Quaternion {
    private String name;

    public Return(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "return " + this.name;
    }

}
