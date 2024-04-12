package com.example.t23new.tree;

public interface Tree<K extends Comparable<K>, V> {

    boolean insert(K key, V value);
    V get(K key);
    boolean remove(K key);
    int size();
    boolean isEmpty();
    boolean set(K key, V value);
    void clear();
}
