package com.thiendz.example.springsocket.configs;

import com.thiendz.example.springsocket.dto.LimitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LimitRequestConfig {
    @Bean
    public LimitRequest limitRequest() {
        log.info("Load Limit Request Config...");
        LimitRequest limitRequest = new LimitRequest()
                .loadConfig();
        log.info("Load Limit Request Success.");
        return limitRequest;
    }
}
