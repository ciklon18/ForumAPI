package com.forum.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MessageCreateDto(
    @NotBlank(message = "Text is mandatory")
    @Size(min = 1, max = 1000, message = "Text must be between 1 and 1000 characters")
    String text,
    UUID topicId
) {
}
