package table;

import code.StrCounter;

import java.util.HashMap;

public class StrTable {
    public static HashMap<String,String> table = new HashMap<>();


    public void addStr(String s) {
        if (table.get(s) == null) {
            table.put(s,StrCounter.getNewone());
        }
    }

    public static String getName(String content) {
        return table.get(content);
    }



}
