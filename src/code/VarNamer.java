package code;

import java.util.HashMap;

public class VarNamer {
    private static HashMap<String,Integer> nameSet = new HashMap<>();

    public static String getAlias(String name) {
        if (nameSet.get(name) == null) {
            nameSet.put(name,0);
            return name + '.' + 0;
        } else {
            nameSet.put(name,nameSet.get(name) + 1);
            return name + "." + nameSet.get(name);
        }
    }

}
