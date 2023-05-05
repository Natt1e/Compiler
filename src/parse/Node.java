package parse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Node {
    private ArrayList<Node> sons = new ArrayList<>();
    private String name;
    private int row;
    private Node dad;
    private boolean sys;
    private int id;

    public Node(String name,int row,boolean sys) {
        this.name = name;
        this.row = row;
        this.sys = sys;
    }

    public ArrayList<Node> getSons() {
        return this.sons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public boolean getSys() {
        return this.sys;
    }

    public int getRow() {
        return this.row;
    }

    public void addSon(Node node) {
        this.sons.add(node);
        node.addDad(this);
    }

    public void addDad(Node dad) {
        this.dad = dad;
    }

    public Node getDad() {
        return this.dad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

}
