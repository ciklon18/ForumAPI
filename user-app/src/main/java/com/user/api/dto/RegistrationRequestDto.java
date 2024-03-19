package com.user.api.dto;

import com.user.core.constants.DefaultMessages;
import com.user.core.constants.RegularExpressions;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(
        @Pattern(regexp = RegularExpressions.EMAIL, message = DefaultMessages.EMAIL_INVALID)
        String email,

        @Pattern(regexp = RegularExpressions.PASSWORD, message = DefaultMessages.PASSWORD_INVALID)
        String password,
        @NotNull
        @Size(min = 3, max = 30, message = DefaultMessages.LOGIN_INVALID)
        String login,
        String name,
        String surname
) {
}
