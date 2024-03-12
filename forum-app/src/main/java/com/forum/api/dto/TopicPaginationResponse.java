package com.forum.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TopicPaginationResponse(
    Integer pageNumber,
    Integer totalPagesAmount,
    List<TopicDto> topics
) {
}
