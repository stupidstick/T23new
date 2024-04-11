package com.example.t23new.tree;

public class Test {
    public static void main(String[] args) {
        TwoThreeTreeGovno<Integer, String> tree = new TwoThreeTreeGovno(Integer.class);
        tree.insert(10, "A");
        tree.insert(20, "B");
        tree.insert(30, "C");
        tree.insert(40, "D");
        tree.insert(50, "E");
        tree.insert(60, "E");
        tree.insert(70, "E");
        tree.insert(80, "E");
        tree.insert(90, "E");
        tree.remove(30);
        tree.bfsList();
    }
}
