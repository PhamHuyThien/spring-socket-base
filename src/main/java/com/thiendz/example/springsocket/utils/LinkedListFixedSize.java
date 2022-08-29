package com.thiendz.example.springsocket.utils;

import java.util.LinkedList;

public class LinkedListFixedSize<K> extends LinkedList<K> {
    private final int maxSize;

    public LinkedListFixedSize(int size) {
        this.maxSize = size;
    }

    public boolean add(K k) {
        boolean r = super.add(k);
        if (size() > maxSize)
            removeRange(0, size() - maxSize);
        return r;
    }

    public K getYoungest() {
        return get(size() - 1);
    }

    public K getOldest() {
        return get(0);
    }
}
