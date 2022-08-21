package com.thiendz.example.springsocket.dto;

import java.util.HashMap;
import java.util.Map;

public class SqlResource {
    private final Map<String, String> listSqlData;

    public SqlResource(Map<String, String> listSqlData) {
        if (listSqlData == null)
            this.listSqlData = new HashMap<>();
        else
            this.listSqlData = listSqlData;
    }

    public String get(String name) {
        return this.listSqlData.get(name);
    }
}