package com.user.api.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record RoleAssignDto(
        UserRole role,
        @Nullable
        UUID categoryId
) {
}
