package com.forum.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicUpdateDto(
    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    String name,
    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    String description
) {
}
