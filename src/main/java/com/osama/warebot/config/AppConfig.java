package com.osama.warebot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.SubscribableChannel;

@Configuration
public class AppConfig {


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
