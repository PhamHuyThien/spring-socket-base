package com.thiendz.example.springsocket.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlUtils {

    public static <T> T map(String nativeSql, Object[] objects, Class<T> clazz) throws Exception {
        ObjectMapper objectMapper = BeanUtil.getApplicationContext().getBean(ObjectMapper.class);
        List<String> nameList = sqlToSelectName(nativeSql);
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < objects.length; i++)
            map.put(nameList.get(i), objects[i]);
        String json = objectMapper.writeValueAsString(map);
        return objectMapper.readValue(json, clazz);
    }

    public static <T> List<T> map(String nativeSql, List<?> objects, Class<T> clazz) throws Exception {
        ObjectMapper objectMapper = BeanUtil.getApplicationContext().getBean(ObjectMapper.class);
        List<String> nameList = sqlToSelectName(nativeSql);
        List<T> listObjectMapper = new ArrayList<>();
        for (Object object : objects) {
            Object[] objs = (Object[]) object;
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < objs.length; j++)
                map.put(nameList.get(j), objs[j]);
            String json = objectMapper.writeValueAsString(map);
            listObjectMapper.add(objectMapper.readValue(json, clazz));
        }
        return listObjectMapper;
    }

    public static List<String> sqlToSelectName(String sql) {
        sql = sql.toLowerCase();
        int selectId = sql.indexOf("select");
        int fromId = sql.indexOf("from");
        String select = sql.substring(selectId + 6, fromId);
        String[] cols = select.split(",");
        List<String> nameList = new ArrayList<>();
        for (String col : cols) {
            int asId = col.indexOf(" as ");
            if (asId != -1)
                nameList.add(col.substring(asId + 4).trim());
        }
        return nameList;
    }
}
