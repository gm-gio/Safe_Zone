package com.george.fileservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.george.clients")
public class FileServiceApp {

    public static void main(String... args) {
        SpringApplication.run(FileServiceApp.class, args);
    }
}