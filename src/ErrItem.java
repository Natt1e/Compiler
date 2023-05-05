public class ErrItem {
    private final int row;
    private final String type;

    public ErrItem(int row, String type) {
        this.row = row;
        this.type = type;
    }

    public String toString() {
        return this.row + " " + this.type;
    }

    public int getRow() {
        return this.row;
    }

    public String getType() {
        return this.type;
    }


}
