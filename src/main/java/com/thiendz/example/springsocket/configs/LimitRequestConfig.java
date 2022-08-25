package com.thiendz.example.springsocket.configs;

import com.thiendz.example.springsocket.dto.LimitRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LimitRequestConfig {
    @Bean
    public LimitRequest limitRequest(){
        return new LimitRequest()
                .loadConfig();
    }
}
