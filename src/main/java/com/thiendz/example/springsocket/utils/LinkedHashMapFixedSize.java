package com.thiendz.example.springsocket.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapFixedSize<D, T> extends LinkedHashMap<D, T> {

    private final int maxSize;

    public LinkedHashMapFixedSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<D, T> eldest) {
        return this.size() > this.maxSize;
    }
}
