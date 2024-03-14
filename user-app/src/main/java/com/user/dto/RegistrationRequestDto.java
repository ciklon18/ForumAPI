package com.user.dto;

import com.user.constants.DefaultMessages;
import com.user.constants.RegularExpressions;
import jakarta.validation.constraints.Pattern;

public record RegistrationRequestDto(
        @Pattern(regexp = RegularExpressions.EMAIL, message = DefaultMessages.EMAIL_INVALID)
        String email,

        @Pattern(regexp = RegularExpressions.PASSWORD, message = DefaultMessages.PASSWORD_INVALID)
        String password,
        String login,
        String name,
        String surname
) {
}
