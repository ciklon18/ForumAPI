package com.user.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.user.api.dto.UserDto;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.ModeratorRepository;
import com.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IntegrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ModeratorRepository moderatorRepository;

    public Boolean isUserExist(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::map)
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "User not found"));
    }

    @Transactional(readOnly = true)
    public List<UUID> getModeratorCategoriesByUserId(UUID userId) {
        return moderatorRepository.findAllByUserId(userId)
                .stream()
                .toList();
    }
}
