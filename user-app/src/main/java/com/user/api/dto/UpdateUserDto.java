package com.user.api.dto;

public record UpdateUserDto(
        String login,
        String password,
        String email,
        String name,
        String surname
) {
}
