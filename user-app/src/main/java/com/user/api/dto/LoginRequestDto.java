package com.user.api.dto;

import com.user.core.constants.DefaultMessages;
import com.user.core.constants.RegularExpressions;
import jakarta.validation.constraints.Pattern;

public record LoginRequestDto(
        @Pattern(regexp = RegularExpressions.EMAIL, message = DefaultMessages.EMAIL_INVALID)
        String email,
        @Pattern(regexp = RegularExpressions.PASSWORD, message = DefaultMessages.PASSWORD_INVALID)
        String password) {
}
