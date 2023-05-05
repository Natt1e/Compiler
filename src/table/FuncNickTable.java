package table;

import java.util.HashMap;

public class FuncNickTable {
    public static HashMap<String,FuncNickItem> referenceTable = new HashMap<>(); //通过别名 查 函数体
    public static HashMap<String,String> nameTable = new HashMap<>();//通过本名查别名

    public static void addFunc(String funcname,boolean isReturn) {
        nameTable.put(funcname,FuncNickCounter.getNickName(funcname));
        FuncNickItem item = new FuncNickItem(FuncNickCounter.getNickName(funcname),isReturn);
        referenceTable.put(FuncNickCounter.getNickName(funcname),item);
    }

    public static String getNickName(String funcname) {
        return nameTable.get(funcname);
    }

    public static boolean getIsReturn(String name) {
        return referenceTable.get(name).getIsReturn();
    }

}
