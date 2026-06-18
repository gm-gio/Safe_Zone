package com.george.clients.urlShortener;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "url-shortener")
public interface ShortenerClient {
    @PostMapping("/api/v1/responses/generate/{id}")
    ResponseEntity<UrlsResponse> generate(
            @PathVariable("id") Long responseId
    );
}
