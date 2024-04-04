package com.user.integration.client.forum;

import com.common.integration.config.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "forum-app", url = "http://localhost:9445", configuration = ClientConfig.class)
public interface ForumClient {
    @GetMapping("/integration/category/{id}")
    boolean isCategoryExist(@PathVariable UUID id);
}
