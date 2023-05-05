package actuator;

import parse.Node;
import table.StrTable;

public class StringActuator {
    public static StrTable strTable = new StrTable();

    public static void processStr(Node node) {
        recursion(node);
    }

    public static void recursion(Node node) {
        if (node.getName().equals("printf") && node.getSys()) {
            generateStr(node);
        }
        for (Node son : node.getSons()) {
            recursion(son);
        }
    }

    public static void generateStr(Node node) {
        Node formatString = node.getDad().getSons().get(2);
        String str = formatString.getSons().get(0).getName();
        str = str.substring(1,str.length()-1);
        String[] strs = str.split("%d");
        for (String s : strs) {
            strTable.addStr(s);
        }
    }

    public static void printStrs() {
        for (String key : StrTable.table.keySet()) {
            String content = StrTable.table.get(key) + ": .asciiz " + '"' + key + '"';
            Actuator.writeToMips(content);
        }
    }

}
