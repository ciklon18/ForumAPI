package com.user.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record AuthorityResponse(

        UUID userId,

        List<AuthorityType> authorities
) {
}
