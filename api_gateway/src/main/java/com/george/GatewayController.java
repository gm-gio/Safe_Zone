package com.george;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @RequestMapping("/")
    public String home() {
        return "Welcome to the API Gateway! nginx";
    }
}
