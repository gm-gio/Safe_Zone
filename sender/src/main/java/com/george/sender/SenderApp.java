package com.george.sender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.george.clients")
@SpringBootApplication
public class SenderApp {
    public static void main(String... args){
        SpringApplication.run(SenderApp.class, args);
    }
}