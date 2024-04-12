package com.example.t23new.tree;

public interface Iterator<K extends Comparable<K>, V> {

    V get();
    void set(V val);

    void next();
    void prev();

    boolean inBounds();
}
