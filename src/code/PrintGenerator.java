package code;

import calculation.AddExpor;
import parse.Node;
import quaternion.End;
import quaternion.PrintInterger;
import quaternion.PrintStr;
import quaternion.Quaternion;

import java.util.ArrayList;

public class PrintGenerator {

    public static void processPrintf(Node format, ArrayList<Node> exps) {
        int k = 0;
        String str = format.getSons().get(0).getName();
        str = str.substring(1,str.length()-1); // 去引号
        StringBuilder temp = new StringBuilder();
        ArrayList<Quaternion> quaternions = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '%') {
                PrintStr p1 = new PrintStr(temp.toString());
                quaternions.add(p1);

                temp = new StringBuilder();
                PrintInterger p2 = new PrintInterger(AddExpor.calAddExpvr(exps.get(k)));
                quaternions.add(p2);

                k++;
                i++;
            } else {
                temp.append(str.charAt(i));
            }
        }
        if (temp.length() != 0) {
            PrintStr p1 = new PrintStr(temp.toString());
            quaternions.add(p1);
        }
        CodeGenerator.quaternions.addAll(quaternions);
        End end = new End();
        CodeGenerator.quaternions.add(end);
    }

}
