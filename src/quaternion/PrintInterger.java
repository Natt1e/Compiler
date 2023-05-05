package quaternion;

import code.TempVar;
import parse.Node;

public class PrintInterger implements Quaternion{
    private String name;

    public PrintInterger(TempVar result) {
        name = result.getName();
    }

    public String toString() {
        return "printf " + name;
    }

    public String getName() {
        return this.name;
    }

}
