package com.user.core.service;

import com.common.auth.jwt.Role;
import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BaseUserService {
    private final UserRepository userRepository;
    private final AuthorityService authorityService;

    void isLoginAndEmailAlreadyUsed(String login, String email) {
        if (userRepository.isProfileExistByLoginAndEmail(login, email)) {
            throw new CustomException(ExceptionType.ALREADY_EXISTS, "Login is already used");
        }
    }

    void setNewUserAuthorities(UUID userId) {
        authorityService.setNewUserAuthorities(userId, Role.BASE_ROLE.getAuthority());
    }
}
