package com.george.clients.template;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TEMPLATE")
public interface TemplateClient {

    @GetMapping("/api/v1/templates/{templateId}")
    public TemplateResponse getTemplateById (@PathVariable Long templateId);
}
