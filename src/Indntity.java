import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Indntity {
    private String s;
    private int i;
    private String sym;
    private boolean print = false;//词法分析器输出开关
    private String path = "output.txt";
    private FileWriter fw;
    private BufferedWriter bw;
    private File writefile = new File(path);
    private boolean notes = false;//是否是注释的信号变量
    private ArrayList<String> words;
    private ArrayList<String> kind;
    private int row = 0;
    private ArrayList<Integer> rows;

    public Indntity() {
        this.words = new ArrayList<>();//创建一个新的空的单词表
        this.kind = new ArrayList<>();
        this.rows = new ArrayList<>();//存储行的信息
        if (writefile.exists() == false) {
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
    }

    public ArrayList<String> getWords() {
        return this.words;
    }

    public ArrayList<String> getKind() {
        return this.kind;
    }

    public ArrayList<Integer> getRows() {
        return this.rows;
    }

    public void setS(String s) {
        this.s = s;
        this.i = 0;
        int len = s.length();
        this.row++;
        while (i < len) {
            analyzeWords();//进入词法分析程序,为按行分析
        }
        return;
    }

    public void analyzeWords() {
        sym = "";
        int len = s.length();
        if (notes) {
            simpleSym();
        } else {
            if (s.charAt(i) == '_' || isLetter(s.charAt(i))) {
                while (s.charAt(i) == '_' || isLetter(s.charAt(i)) || isNum(s.charAt(i))) {
                    sym += s.charAt(i);
                    i++;
                    if (i >= len) {
                        break;
                    }
                }
                reversed(sym);
            } else if (isNum(s.charAt(i))) {
                while (isNum(s.charAt(i))) {
                    sym += s.charAt(i);
                    i++;
                    if (i >= len) {
                        break;
                    }
                }
                printTotxt("INTCON ", sym);
            } else if (s.charAt(i) == '"') {
                sym += '"';
                i++;
                while (s.charAt(i) != '"') {
                    sym += s.charAt(i);
                    i++;
                    if (i >= len) {
                        break;
                    }
                }
                sym += s.charAt(i);
                i++;
                printTotxt("STRCON ", sym);
            } else if (s.charAt(i) == ' ') {
                i++;
            } else {
                simpleSym();
            }
        }

    }

    public void simpleSym() {
        int len = s.length();
        if (notes) {
            while (i < len) {
                if (s.charAt(i) == '*') {
                    if (i < len - 1) {
                        if (s.charAt(i + 1) == '/') {
                            notes = false;
                            i += 2;
                            break;
                        }
                    }
                }
                i++;
            }
        } else {
            if (s.charAt(i) == '/') {
                if (i < len - 1) {
                    if (s.charAt(i + 1) == '/') {
                        while (i < len) {
                            i++;
                        }
                    } else if (s.charAt(i + 1) == '*') {
                        notes = true;
                        i += 2;
                    } else {
                        printTotxt("DIV ", "/");
                        i++;
                    }
                } else {
                    printTotxt("DIV ", "/");
                    i++;
                }
            } else {
                notNotes();
            }
        }
    }

    public void notNotes() {
        int len = s.length();
        if (s.charAt(i) == ';') {
            printTotxt("SEMICN ", ";");
        } else if (s.charAt(i) == ',') {
            printTotxt("COMMA ", ",");
        } else if (s.charAt(i) == '(') {
            printTotxt("LPARENT ", "(");
        } else if (s.charAt(i) == ')') {
            printTotxt("RPARENT ", ")");
        } else if (s.charAt(i) == '[') {
            printTotxt("LBRACK ", "[");
        } else if (s.charAt(i) == ']') {
            printTotxt("RBRACK ", "]");
        } else if (s.charAt(i) == '{') {
            printTotxt("LBRACE ", "{");
        } else if (s.charAt(i) == '}') {
            printTotxt("RBRACE ", "}");
        } else if (s.charAt(i) == '%') {
            printTotxt("MOD ", "%");
        } else if (s.charAt(i) == '+') {
            printTotxt("PLUS ", "+");
        } else if (s.charAt(i) == '-') {
            printTotxt("MINU ", "-");
        } else if (s.charAt(i) == '*') {
            printTotxt("MULT ", "*");
        } else if (s.charAt(i) == '&') {
            printTotxt("AND ", "&&");
            i++;
        } else if (s.charAt(i) == '|') {
            printTotxt("OR ", "||");
            i++;
        } else if (s.charAt(i) == '!') {
            if (i < len - 1) {
                if (s.charAt(i + 1) == '=') {
                    printTotxt("NEQ ", "!=");
                    i++;
                } else {
                    printTotxt("NOT ", "!");
                }
            } else {
                printTotxt("NOT ", "!");
            }
        } else if (s.charAt(i) == '<') {
            if (i < len - 1) {
                if (s.charAt(i + 1) == '=') {
                    printTotxt("LEQ ", "<=");
                    i++;
                } else {
                    printTotxt("LSS ", "<");
                }
            } else {
                printTotxt("LSS ", "<");
            }
        } else if (s.charAt(i) == '>') {
            if (i < len - 1) {
                if (s.charAt(i + 1) == '=') {
                    printTotxt("GEQ ", ">=");
                    i++;
                } else {
                    printTotxt("GRE ", ">");
                }
            } else {
                printTotxt("GRE ", ">");
            }
        } else if (s.charAt(i) == '=') {
            if (i < len - 1) {
                if (s.charAt(i + 1) == '=') {
                    printTotxt("EQL ", "==");
                    i++;
                } else {
                    printTotxt("ASSIGN ", "=");
                }
            } else {
                printTotxt("ASSIGN ", "=");
            }
        }
        i++;
    }

    public void reversed(String word) {
        if (word.equals("main")) {
            printTotxt("MAINTK ", word);
        } else if (word.equals("const")) {
            printTotxt("CONSTTK ", word);
        } else if (word.equals("int")) {
            printTotxt("INTTK ", word);
        } else if (word.equals("break")) {
            printTotxt("BREAKTK ", word);
        } else if (word.equals("continue")) {
            printTotxt("CONTINUETK ", word);
        } else if (word.equals("if")) {
            printTotxt("IFTK ", word);
        } else if (word.equals("else")) {
            printTotxt("ELSETK ", word);
        } else if (word.equals("while")) {
            printTotxt("WHILETK ", word);
        } else if (word.equals("getint")) {
            printTotxt("GETINTTK ", word);
        } else if (word.equals("printf")) {
            printTotxt("PRINTFTK ", word);
        } else if (word.equals("return")) {
            printTotxt("RETURNTK ", word);
        } else if (word.equals("void")) {
            printTotxt("VOIDTK ", word);
        } else {
            printTotxt("IDENFR ", word);
        }
    }

    public void printTotxt(String s, String word) {//此为词法分析器向文件输出的函数
        this.words.add(word);//将分析出的单词存到单词表中，为语法分析提供服务
        this.kind.add(s);
        this.rows.add(this.row);
        if (print) {
            try {
                fw = new FileWriter(writefile, true);
                bw = new BufferedWriter(fw);
                fw.write(s + word + '\n');
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isLetter(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return true;
        }
        return false;
    }

    public boolean isNum(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

}
