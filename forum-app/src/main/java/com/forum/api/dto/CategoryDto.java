package com.forum.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CategoryDto(
    String id,
    String name,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<CategoryDto> childCategories
) {
}
