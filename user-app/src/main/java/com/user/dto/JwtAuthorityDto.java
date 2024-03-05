package com.user.dto;


import lombok.Builder;

import java.util.UUID;

@Builder
public record JwtAuthorityDto(

        UUID userId,

        String token

) {
}
