package com.forum.api.rest;

import com.forum.api.constant.IntegrationPaths;
import com.forum.core.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IntegrationController {

    private final IntegrationService integrationService;

    @GetMapping(IntegrationPaths.CATEGORY_BY_ID)
    boolean isCategoryExist(@PathVariable UUID id) {
        return integrationService.isCategoryExist(id);
    }

    @GetMapping(IntegrationPaths.MESSAGE_BY_ID_USER_ID)
    boolean isUserOwnerOfMessage(@PathVariable UUID messageId, @PathVariable UUID userId) {
        log.info("isUserOwnerOfMessage. messageId: {}, userId: {}", messageId, userId);
        return integrationService.isUserOwnerOfMessage(messageId, userId);
    }

    @GetMapping(IntegrationPaths.IS_MODERATOR_BY_MESSAGE_ID)
    boolean isModerator(
            @PathVariable("messageId") UUID messageId,
            @RequestParam("moderatorCategoryIds") List<UUID> moderatorCategoryIds
    ) {
        return integrationService.isModerator(messageId, moderatorCategoryIds);
    }
}
