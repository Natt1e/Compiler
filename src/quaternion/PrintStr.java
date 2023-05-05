package quaternion;

import table.StrTable;

public class PrintStr implements Quaternion{
    private final String content;

    public PrintStr(String content) {
        this.content = content;
    }

    public String toString() {
        return "printf " + content;
    }

    public String getStrName() {
        return StrTable.getName(content);
    }

    public String getContent() {
        return this.content;
    }

}
