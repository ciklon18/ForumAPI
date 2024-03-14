package com.user.dto;


import com.forum.security.dto.AuthorityType;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record JwtAuthorityDto(
        UUID userId,
        String token,
        List<AuthorityType> authorities

) {
}
