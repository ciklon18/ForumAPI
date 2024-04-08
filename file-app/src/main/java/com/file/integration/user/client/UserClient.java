package com.file.integration.user.client;

import com.common.integration.config.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "user-app", url = "http://localhost:9444", configuration = ClientConfig.class)
public interface UserClient {
    @GetMapping("/integration/user/{userId}/moderator/category")
    List<UUID> getModeratorCategoriesByUserId(@PathVariable UUID userId);
}
