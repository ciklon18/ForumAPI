package com.common.kafka.dto;

import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder

public record MessageDto(
        String header,
        String text,
        List<String> userEmails
) implements Serializable {
}
