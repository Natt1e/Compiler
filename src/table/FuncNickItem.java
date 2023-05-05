package table;

public class FuncNickItem {
    private String funcName; //NickName
    private boolean isReturn; // void or int


    public FuncNickItem(String funcName,boolean isReturn) {
        this.funcName = funcName;
        this.isReturn = isReturn;
    }

    public boolean getIsReturn() {
        return this.isReturn;
    }

}
