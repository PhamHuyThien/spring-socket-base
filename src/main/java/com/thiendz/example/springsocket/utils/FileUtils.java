package com.thiendz.example.springsocket.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    public static String getSqlFileResource(String fileName) throws IOException {
        Resource resource = new ClassPathResource("/sql/" + fileName + ".sql");
        InputStream inputStream = resource.getInputStream();
        StringBuilder data = new StringBuilder();
        int k;
        while ((k = inputStream.read()) != -1)
            data.append((char) k);
        return data.toString();
    }
}
