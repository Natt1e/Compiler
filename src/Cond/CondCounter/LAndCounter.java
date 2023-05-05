package Cond.CondCounter;

public class LAndCounter {
    private static int no = -1;

    public static void reset() {
        no = -1;
    }

    public static int getNewNo() {
        no++;
        return no;
    }

}
