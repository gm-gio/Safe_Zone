package com.george.template;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.george.clients")
public class TemplateApp {
    public static void main(String... args){
        SpringApplication.run(TemplateApp.class, args);
    }
}