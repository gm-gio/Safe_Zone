package com.george.rebalancer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.george.clients")
@SpringBootApplication
public class RebalancerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RebalancerApplication.class, args);
    }
}