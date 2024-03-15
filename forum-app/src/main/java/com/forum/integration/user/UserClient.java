package com.forum.integration.user;

import com.forum.config.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8081", configuration = ClientConfig.class)
public interface UserClient {
    @GetMapping("/api/user/check/{id}")
    Boolean checkUserExisingById(@PathVariable UUID id);
}
