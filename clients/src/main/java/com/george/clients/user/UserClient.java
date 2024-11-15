package com.george.clients.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user",
        url = "${clients.user.url}"
)
public interface UserClient {

    @GetMapping("/api/v1/users/{userId}")
     public UserResponse getUserById (@PathVariable  Long userId);
}
