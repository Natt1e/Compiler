package Cond;

import java.util.Stack;

public class WhileStack {
    private static Stack<Integer> whiles = new Stack<>();

    public static void push(int no) {
        whiles.push(no);
    }

    public static void pop() {
        whiles.pop();
    }

    public static int getCurent() {
        return whiles.peek();
    }

}
