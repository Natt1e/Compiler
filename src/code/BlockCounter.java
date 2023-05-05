package code;

public class BlockCounter {
    private static int count = -1;

    public static int getBlockCount() {
        count++;
        return count;
    }

}
