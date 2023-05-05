package code;

public class StrCounter {
    private static int no = -1;

    public static void reset() {
        no = -1;
    }

    public static String getNewone() {
        no++;
        return "str_" + no;
    }
}
