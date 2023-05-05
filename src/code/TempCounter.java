package code;

public class TempCounter {
    private static int no = -1;

    public static void reset() {
        no = -1;
    }

    public static int getNo() {
        no++;
        return no;
    }


}
