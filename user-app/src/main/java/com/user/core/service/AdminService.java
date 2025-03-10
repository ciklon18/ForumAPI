package com.user.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.common.kafka.enums.NotificationType;
import com.user.api.dto.RegistrationRequestDto;
import com.user.api.dto.UpdateUserDto;
import com.user.api.dto.UserDto;
import com.user.core.constants.DefaultMessages;
import com.user.core.entity.User;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.UserRepository;
import com.user.integration.notification.NotificationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class AdminService extends BaseUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    public AdminService(
            UserRepository userRepository,
            UserMapper userMapper,
            BCryptPasswordEncoder passwordEncoder,
            AuthorityService authorityService,
            NotificationService notificationService
    ) {
        super(userRepository, authorityService);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
    }

    @Transactional
    public UserDto createUser(RegistrationRequestDto registrationRequestDto) {
        isLoginAndEmailAlreadyUsed(registrationRequestDto.login(), registrationRequestDto.email());
        User user = userMapper.map(registrationRequestDto, passwordEncoder.encode(registrationRequestDto.password()));
        User savedUser = userRepository.save(user);
        setNewUserAuthorities(savedUser.getId());
        return userMapper.map(savedUser);
    }

    @Transactional
    public void updateUser(UUID userId, UpdateUserDto updateUserDto) {
        if (updateUserDto.login() != null || updateUserDto.email() != null) {
            isLoginAndEmailAlreadyUsed(updateUserDto.login(), updateUserDto.email());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "User is not found"));
        User updatedUser = userMapper.map(user, updateUserDto, passwordEncoder.encode(updateUserDto.password()));
        userRepository.save(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "User is not found"));
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void blockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "User is not found"));
        user.setBlockedAt(LocalDateTime.now());
        userRepository.save(user);
        sendNotificationToBlockedUser(user.getId().toString());
    }

    private void sendNotificationToBlockedUser(String userId) {
        notificationService.sendNotification(
                DefaultMessages.USER_BLOCK_HEADER,
                DefaultMessages.USER_BLOCK_TEXT,
                List.of(userId),
                NotificationType.ALL
        );
    }
}
