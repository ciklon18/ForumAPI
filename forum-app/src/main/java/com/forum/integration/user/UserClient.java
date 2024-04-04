package com.forum.integration.user;

import com.common.integration.config.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-app", url = "http://localhost:9444", configuration = ClientConfig.class)
public interface UserClient {
    @GetMapping("/integration/user/check/{id}")
    Boolean isUserExist(@PathVariable UUID id);
}
