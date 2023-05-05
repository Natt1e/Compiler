package table;

import java.util.ArrayList;

public class AliasItem {
    private final String name;
    private final boolean isConst;
    private final int addr;
    private final int dimension1; // 保存一维数组的元素数量
    private int dimension2;
    private ArrayList<Integer> value;
    private boolean isGlobal;
    private boolean isLocal;    //是否为本地变量

    public AliasItem(String name,boolean isConst,int addr,int dimension1,int dimension2,boolean isGlobal) {
        this.name = name;
        this.isConst = isConst;
        this.addr = addr;
        this.dimension1 = dimension1;
        this.dimension2 = dimension2;
        this.value = new ArrayList<>();
        this.isGlobal = isGlobal;
        this.isLocal = true;
    }

    public void setSingleValue(int value) { //适用于普通变量 即非数组
        this.value.add(value);
    }

    public int getSingleValue() {
        return this.value.get(0);
    }

    public void setValues(ArrayList<Integer> values) {
        this.value = values;
    }

    public ArrayList<Integer> getValues() {
        return this.value;
    }

    public void addValue(int value) {
        this.value.add(value);
    }

    public boolean getIsConst() {
        return this.isConst;
    }

    public String getName() {
        return this.name;
    }

    public int getAddr() {
        return this.addr;
    }

    public boolean getIsGlobal() {
        return this.isGlobal;
    }

    public int getDimension2() {
        return this.dimension2;
    }

    public int getDimension1() {
        return this.dimension1;
    }

    public void setIsLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public boolean getIsLocal() {
        return this.isLocal;
    }

}
