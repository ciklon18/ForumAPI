package com.user.api.dto;

import java.util.UUID;

public record LogoutRequestDto(
    UUID userId
) {
}
