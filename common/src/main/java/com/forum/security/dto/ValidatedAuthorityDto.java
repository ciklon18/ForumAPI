package com.forum.security.dto;

import java.util.List;
import java.util.UUID;

public record ValidatedAuthorityDto(

        UUID userId,

        List<String> authorities
) {
}
