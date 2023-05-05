package table;

import java.util.HashMap;

public class SymbolTable {
    private final HashMap<String,SymbolItem> table;
    private final SymbolTable father;

    public SymbolTable(SymbolTable father) {
        this.father = father;
        this.table = new HashMap<>();
    }

    public void setAlias(String name,String alias) {
        SymbolTable cur = this;
        cur.getTable().get(name).setAlias(alias);
    }

    public String getAlias(String name) { //默认一定存在该变量
        SymbolTable cur = this;
        while (cur.getTable().get(name) == null) {
            cur = cur.getFather();
        }
        return cur.getTable().get(name).getAlias();
    }

    public SymbolTable getFather() {
        return this.father;
    }

    public HashMap<String,SymbolItem> getTable() {
        return this.table;
    }

    public boolean addSymbol(String name,int type,boolean t) { // 添加失败返回false 添加更改返回true
        if (this.table.get(name) != null) { // 重复定义
            return false;
        }
        SymbolItem item = new SymbolItem(name,type,t);
        table.put(name,item);
        return true;
    }

    public int getType(String name) {
        SymbolTable cur = this;
        while (cur.getTable().get(name) == null) {
            cur = cur.getFather();
        }
        return cur.getTable().get(name).getType();
    }

    public boolean ifConst(String name) {
        SymbolTable cur = this;
        while (cur.getTable().get(name) == null) {
            cur = cur.getFather();
        }
        return cur.getTable().get(name).getIf();
    }

    public boolean findSymbol(String name) {
        SymbolTable cur = this;
        while (cur.getTable().get(name) == null) {
            cur = cur.getFather();
            if (cur == null) {
                break;
            }
        }
        return cur != null;
    }

}
