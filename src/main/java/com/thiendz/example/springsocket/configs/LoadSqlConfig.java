package com.thiendz.example.springsocket.configs;

import com.thiendz.example.springsocket.dto.SqlResource;
import com.thiendz.example.springsocket.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class LoadSqlConfig {

    @Bean
    public SqlResource getSqlData() throws IOException {
        log.info("Loading all sql resource....");
        Map<String, String> listSqlData = new HashMap<>();
        Resource resource = new ClassPathResource("/sql/");
        String[] listFileName = resource.getFile().list();
        if (listFileName != null) {
            for (String sqlFile : listFileName)
                if (sqlFile.endsWith(".sql")) {
                    String name = sqlFile.substring(0, sqlFile.lastIndexOf("."));
                    String data = FileUtils.getSqlFileResource(name);
                    listSqlData.put(name, data);
                }
            log.info("Loaded " + listFileName.length + " resource file success");
        }
        return new SqlResource(listSqlData);
    }
}


