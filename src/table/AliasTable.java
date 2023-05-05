package table;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

public class AliasTable {
    private static HashMap<String,AliasItem> table;

    public AliasTable() {
        AliasTable.table = new HashMap<>();
    }

    public void addAlias(String name,boolean isConst,int addr,int d1,int d2,boolean isGlobal) {
        AliasItem item = new AliasItem(name,isConst,addr,d1,d2,isGlobal);
        AliasTable.table.put(name,item);
    }

    public void setSingleValue(String name,int value) {
        AliasTable.table.get(name).setSingleValue(value);
    }

    public static void addValues(String name, ArrayList<Integer> values) {
        AliasTable.table.get(name).setValues(values);
    }

    public int getSingleValue(String name) {
        return table.get(name).getSingleValue();
    }

    public static ArrayList<Integer> getValues(String name) {
        return table.get(name).getValues();
    }

    public static void addValue(String name,int value) {
        AliasTable.table.get(name).addValue(value);
    }

    public static HashMap<String, AliasItem> getTable() {
        return table;
    }

    public static int getAddr(String name) {
        return AliasTable.table.get(name).getAddr();
    }

    public static boolean isConst(String name) {
        return AliasTable.table.get(name).getIsConst();
    }

    public static boolean isGlobal(String name) {
        return AliasTable.table.get(name).getIsGlobal();
    }

    public static int getDimension2(String name) {
        return AliasTable.table.get(name).getDimension2();
    }

    public static int getDimension1(String name) {
        return AliasTable.table.get(name).getDimension1();
    }

    public static void setIsLocal(String name, boolean isLocal) {
        AliasTable.table.get(name).setIsLocal(isLocal);
    }

    public static boolean getIsLocal(String name) {
        return AliasTable.table.get(name).getIsLocal();
    }

}
