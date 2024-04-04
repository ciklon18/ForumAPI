package com.user.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.user.api.dto.UserDto;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Boolean isUserExist(UUID userId) {
        return userRepository.existsById(userId);
    }

    public UserDto getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::map)
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "User not found"));
    }
}
