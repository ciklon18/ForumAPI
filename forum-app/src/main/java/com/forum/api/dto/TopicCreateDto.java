package com.forum.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TopicCreateDto(
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    String name,
    @NotBlank(message = "Description is mandatory")
    @Size(min = 3, max = 255, message = "Description must be between 3 and 255 characters")
    String description,
    UUID categoryId
) {
}
