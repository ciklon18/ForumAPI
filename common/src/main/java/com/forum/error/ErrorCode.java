package com.forum.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

        INTERNAL_ERROR("400"),

        SERVICE_UNAVAILABLE("401"),

        AUTH_ERROR("401"),

        INVALID_TOKEN("403"),

        INVALID_ARGUMENT("404");

        private final String code;
}
