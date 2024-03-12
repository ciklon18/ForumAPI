package com.forum.api.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CategoryCreateDto(
    @NotNull(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    String name,
    UUID parentId
) {
}
