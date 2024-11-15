package com.george;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.george.clients")
public class GroupApp {
    public static void main(String[] args) {
        SpringApplication.run(GroupApp.class, args);
    }
}