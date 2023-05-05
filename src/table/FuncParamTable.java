package table;

import java.util.ArrayList;

public class FuncParamTable {
    public boolean empty = true;
    private ArrayList<Integer> type = new ArrayList<>();  //维 度
    // 0 : 变量or常数
    // > 0一维数组
    // <0 二维
    private ArrayList<String> name = new ArrayList<>(); // 形参名



    public void addParamTable(int type,String name) {
        empty = false;
        this.type.add(type);
        this.name.add(name);
    }

    public String getName(int i) {
        return name.get(i);
    }


    public int getType(int i) {
        return type.get(i);
    }

    public int getSum() {
        return type.size();
    } // 获得参数个数

}
