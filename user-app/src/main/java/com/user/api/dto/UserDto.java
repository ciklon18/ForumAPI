package com.user.api.dto;

import lombok.Builder;

import java.util.UUID;


@Builder
public record UserDto(
    UUID id,
    String name,
    String email,
    String login,
    String createdAt,
    String updatedAt,
    String deletedAt,
    String blockedAt
) {

}