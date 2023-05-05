package quaternion;

import java.util.ArrayList;

public class VarDef implements Quaternion {
    private final int dimension;
    private final String varName;
    private ArrayList<String> value;

    public VarDef(String varName, int dimension) {
        this.varName = varName;
        this.dimension = dimension;
        this.value = new ArrayList<>();
    }

    public void addValue(String value) {
        this.value.add(value);
    }

    public boolean isInitialed() {
        if (value.size() == 0) {
            return false;
        }
        return true;
    }

    public int getDimension() {
        return this.dimension;
    }

    public String getVarName() {
        return this.varName;
    }

    public ArrayList<String> getValues() {
        return this.value;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Def " + varName);
        if (value.size() == 0) {
            return stringBuilder.toString();
        } else {
            for (String value : this.value) {
                stringBuilder.append(" ").append(value);
            }
            return stringBuilder.toString();
        }
    }

}
