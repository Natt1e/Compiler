package table;


public class SymbolItem {
    private final String name;
    private final int type;
    private final boolean ifConst;
    // 0 : ! array
    // >0 : 一维
    // < 0 : array 二维
    private String alias;

    public SymbolItem(String name,int type,boolean t) {
        this.name = name;
        this.type = type;
        this.ifConst = t;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return this.alias;
    }

    public int getType() {
        return this.type;
    }

    public boolean getIf() {
        return this.ifConst;
    }

}
