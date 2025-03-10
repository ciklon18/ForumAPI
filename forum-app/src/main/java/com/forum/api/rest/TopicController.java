package com.forum.api.rest;

import com.forum.api.constant.ApiPaths;
import com.forum.api.dto.TopicCreateDto;
import com.forum.api.dto.TopicDto;
import com.forum.api.dto.TopicPaginationResponse;
import com.forum.api.dto.TopicUpdateDto;
import com.forum.core.service.TopicService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping(ApiPaths.TOPIC)
    public UUID createTopic(
            @Valid @RequestBody TopicCreateDto topic
    ) {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return topicService.createTopic(topic, userId);
    }

    @PatchMapping(ApiPaths.TOPIC_BY_ID)
    public void updateTopic(
            @PathVariable UUID topicId,
            @Valid @RequestBody TopicUpdateDto topic
    ) {
        topicService.updateTopic(topicId, topic);
    }

    @DeleteMapping(ApiPaths.TOPIC_BY_ID)
    public void deleteTopic(@PathVariable UUID topicId) {
        topicService.deleteTopic(topicId);
    }

    @GetMapping(ApiPaths.TOPIC)
    public TopicPaginationResponse getTopics(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        pageSize = pageSize > 0 ? pageSize : TopicService.DEFAULT_PAGE_SIZE;
        pageNumber = pageNumber > 0 ? pageNumber : TopicService.DEFAULT_PAGE_NUMBER;
        return topicService.getTopics(pageNumber, pageSize);
    }

    @GetMapping(ApiPaths.TOPIC_BY_QUERY)
    public List<TopicDto> getTopicsByQuery(@RequestParam(required = false) String text) {
        return topicService.getTopicsByQuery(text);
    }
}
