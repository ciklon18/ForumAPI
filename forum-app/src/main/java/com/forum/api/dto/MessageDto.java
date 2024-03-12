package com.forum.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageDto(
    UUID id,
    String text,
    UUID topicId,
    UUID authorId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
