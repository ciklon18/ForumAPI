package com.user.api.dto;


public record TokenDto(
        String accessToken,
        String refreshToken
) {
}
