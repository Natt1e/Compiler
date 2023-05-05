package table;

import java.util.ArrayList;

public class FuncNameTable {
    public static ArrayList<String> funcNames = new ArrayList<>();
    public static ArrayList<String> funcType = new ArrayList<>(); // int or void
    public static ArrayList<FuncParamTable> funcParam = new ArrayList<>();

    public static void addFuncName(String type,String name) {  //新增函数，添加函数名
        funcNames.add(name);
        funcType.add(type);
        FuncParamTable table = new FuncParamTable();
        funcParam.add(table);
    }

    public static FuncParamTable getLastPT() {
        int len = funcParam.size() - 1;
        return funcParam.get(len);
    }


    public static int ifFunc(String name) {
        int len = funcNames.size();
        for (int i = 0;i < len;i++) {
            if (name.equals(funcNames.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static int getParamSum(String name) {
        int len = funcNames.size();
        int i;
        for (i = 0;i < len;i++) {
            if (name.equals(funcNames.get(i))) {
                break;
            }
        }
        return funcParam.get(i).getSum();
    }

    public static FuncParamTable getParams(String name) {
        int len = funcNames.size();
        int i;
        for (i = len - 1;i > 0;i--) {
            if (name.equals(funcNames.get(i))) {
                break;
            }
        }
        return funcParam.get(i);
    }

    public static String getFuncType(String name) {
        int len = funcNames.size();
        int i;
        for (i = 0;i < len;i++) {
            if (name.equals(funcNames.get(i))) {
                break;
            }
        }
        return funcType.get(i);
    }

}
