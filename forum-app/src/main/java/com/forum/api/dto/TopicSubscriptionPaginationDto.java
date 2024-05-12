package com.forum.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TopicSubscriptionPaginationDto(
        Integer pageNumber,
        Integer totalPagesAmount,
        List<TopicSubscriptionDto> topicSubscriptions
) {
}
