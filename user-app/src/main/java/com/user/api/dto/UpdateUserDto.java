package com.user.api.dto;

import com.user.core.constants.DefaultMessages;
import com.user.core.constants.RegularExpressions;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Size(min = 3, max = 30, message = DefaultMessages.LOGIN_INVALID)
        String login,
        @Pattern(regexp = RegularExpressions.PASSWORD, message = DefaultMessages.PASSWORD_INVALID)
        String password,
        @Pattern(regexp = RegularExpressions.EMAIL, message = DefaultMessages.EMAIL_INVALID)
        String email,
        String name,
        String surname,
        @Pattern(regexp = RegularExpressions.PHONE, message = DefaultMessages.PHONE_INVALID)
        String phone
) {
}
