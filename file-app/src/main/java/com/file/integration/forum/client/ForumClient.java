package com.file.integration.forum.client;

import com.common.integration.config.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "forum-app", url = "http://localhost:9445", configuration = ClientConfig.class)
public interface ForumClient {
    @GetMapping("/integration/message/{messageId}/user/{userId}/owner")
    boolean isUserOwnerOfMessage(@PathVariable UUID messageId, @PathVariable UUID userId);

    @GetMapping("/integration/message/{messageId}/moderator")
    boolean isModerator(
            @PathVariable UUID messageId,
            @RequestParam("moderatorCategoryIds") List<UUID> moderatorCategoryIds
    );
}
