import code.CodeGenerator;
import parse.Gramma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Compiler {
    public static void main(String[] argvs) {
        File file = new File("testfile.txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            Indntity indntity = new Indntity();
            while ((s = br.readLine()) != null) {
                indntity.setS(s);//按行进入词法分析程序
            }
            Gramma gramma = new Gramma(indntity.getWords(),indntity.getKind(),indntity.getRows());//初始化语法分析器,传入单词表
            gramma.CompUnit();
            Error error = new Error(gramma.getAncestor());
            error.ErrorMain();//错误处理
            CodeGenerator codeGenerator = new CodeGenerator(gramma.getAncestor());
            codeGenerator.generate();//生成中间代码
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
