package com.hasangurbuz.vehiclemanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {

    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(Logger.class);
    }

    public void e(){
        this.getLogger().error("");
    }
}
