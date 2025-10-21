package com.osama.warebot;

import com.osama.warebot.config.MqttMessageHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.SubscribableChannel;

@SpringBootApplication
public class WareBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(WareBotApplication.class, args);
    }
    @Bean
    public CommandLineRunner init() {
        return args -> {
            System.out.println("🤖 Robot Monitoring Server Started!");
            System.out.println("📍 API: http://localhost:8080/api");
            System.out.println("📊 Actuator: http://localhost:8080/actuator");
            System.out.println("📈 Metrics: http://localhost:8080/actuator/prometheus");
        };
    }


}
