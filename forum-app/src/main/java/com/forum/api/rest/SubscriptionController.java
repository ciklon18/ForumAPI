package com.forum.api.rest;

import com.forum.api.constant.ApiPaths;
import com.forum.api.dto.TopicSubscriptionPaginationDto;
import com.forum.core.service.MessageService;
import com.forum.core.service.TopicSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@RequiredArgsConstructor
public class SubscriptionController {

    private final TopicSubscriptionService subscriptionService;

    @PostMapping(ApiPaths.TOPIC_BY_ID_SUBSCRIBE)
    public void subscribeToTopic(@PathVariable("topicId") UUID topicId) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        subscriptionService.subscribeToTopic(topicId, userId);
    }

    @PostMapping(ApiPaths.TOPIC_BY_ID_UNSUBSCRIBE)
    public void unsubscribeFromTopic(@PathVariable("topicId") UUID topicId) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        subscriptionService.unsubscribeFromTopic(topicId, userId);
    }

    @GetMapping(ApiPaths.TOPIC_SELECTION)
    public TopicSubscriptionPaginationDto getTopicSelections(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        pageNumber = pageNumber > 0 ? pageNumber : MessageService.DEFAULT_PAGE_NUMBER;
        pageSize = pageSize > 0 ? pageSize : MessageService.DEFAULT_PAGE_SIZE;
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return subscriptionService.getTopicSelections(userId, pageNumber, pageSize);
    }
}
