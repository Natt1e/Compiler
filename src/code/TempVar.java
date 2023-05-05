package code;

public class TempVar {
    private String name;
    private int value;

    public TempVar() {
        this.name = "temp.." + TempCounter.getNo();
    }

    public TempVar(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static int getNo(String name) {
        return Integer.parseInt(name.substring(6)) + 1;
    }

    public void setName(String name) {
        this.name = name;
    }

}
