package com.forum.api.dto;

import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.UUID;

public record TopicDto(
    UUID id,
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,}$", message = "Invalid name")
    String name,
    String description,
    UUID categoryId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
