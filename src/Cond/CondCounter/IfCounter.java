package Cond.CondCounter;

public class IfCounter {
    private static int no = -1;

    public static int getCurrentNo() {
        return no;
    }

    public static int getNewIfNo() {
        no++;
        LAndCounter.reset();
        return no;
    }

}
