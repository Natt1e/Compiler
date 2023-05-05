package actuator;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class Actuator {
    public static String path = "mips.txt";
    private static final boolean print = true;
    private static File writefile = new File(path);

    public static void initialization() { //创建mips.txt
        if (!writefile.exists()) {
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            writefile.delete();
            try {
                writefile.createNewFile();
                writefile = new File(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//处理文件,若文件不存在则创建,若文件存在则删除重建
        writeToMips(".data");
    }

    public static String getArrayName(String ident) {
        int index = ident.indexOf("[");
        if (index == -1) {
            return ident;
        }
        return ident.substring(0, index);
    }


    public static void writeToMips(String content) {
        if (print) {
            try {
                FileWriter fw = new FileWriter(writefile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                fw.write(content + '\n');
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
