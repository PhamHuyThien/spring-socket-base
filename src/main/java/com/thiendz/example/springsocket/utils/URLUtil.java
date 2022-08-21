package com.thiendz.example.springsocket.utils;

import java.util.HashMap;
import java.util.Map;

public class URLUtil {
    public static Map<String, String> parseQueryParam(String queryParam){
        Map<String, String> params = new HashMap<>();
        String[] paramSplits = queryParam.split("&");
        for(String paramSplit : paramSplits){
            String[] keyAndValue = paramSplit.split("=");
            String key = keyAndValue[0].trim().toLowerCase();
            String value = "";
            if(keyAndValue.length > 1)
                value = keyAndValue[1].trim();
            params.put(key, value);
        }
        return  params;
    }
}
