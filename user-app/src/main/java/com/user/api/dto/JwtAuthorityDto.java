package com.user.api.dto;


import lombok.Builder;

import java.util.UUID;

@Builder
public record JwtAuthorityDto(
        UUID userId,
        String token
) {
}
