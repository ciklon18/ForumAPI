package com.forum.api.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record MessagePaginationResponse(
    Integer pageNumber,
    Integer totalPagesAmount,
    List<MessageDto> messages
) {
}
