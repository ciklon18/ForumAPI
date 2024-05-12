package com.forum.api.dto;

import java.util.UUID;

public record TopicSubscriptionDto(
        UUID topicId,
        UUID userId,
        String name
) {
}
