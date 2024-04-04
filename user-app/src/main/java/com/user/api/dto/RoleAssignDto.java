package com.user.api.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record RoleAssignDto(
        UUID userId,
        String role,
        @Nullable
        UUID categoryId
) {
}
