package quaternion;

import code.TempCounter;

public class End implements Quaternion {
    private int counts;

    public End() {
        this.counts = TempCounter.getNo();
        TempCounter.reset();
    }

    public String toString() {
        return "";
    }

    public int getCounts() {
        return this.counts;
    }

}
