package com.user.api.dto;

import com.user.core.constants.DefaultMessages;
import com.user.core.constants.RegularExpressions;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotNull
        @Size(min = 3, max = 30, message = DefaultMessages.LOGIN_INVALID)
        String login,
        @Pattern(regexp = RegularExpressions.PASSWORD, message = DefaultMessages.PASSWORD_INVALID)
        String password) {
}
