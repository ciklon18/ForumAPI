package com.user.core.service;

import com.common.auth.annotation.EnableJwtUtils;
import com.common.auth.jwt.Role;
import com.common.auth.util.JwtUtils;
import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.user.api.dto.*;
import com.user.core.entity.User;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableJwtUtils
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityService authorityService;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtAuthorityDto register(RegistrationRequestDto registrationRequestDto) {
        isLoginAlreadyUsed(registrationRequestDto.login());
        User user = userMapper.map(registrationRequestDto, passwordEncoder.encode(registrationRequestDto.password()));
        userRepository.save(user);
        User savedUser = userRepository.findByLogin(user.getLogin());
        String accessToken = jwtUtils.generateAccessToken(user.getLogin(),
                                                          savedUser.getId(),
                                                          List.of(Role.BASE_ROLE.getAuthority())
        );
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), savedUser.getId());
        jwtUtils.saveToken(savedUser.getId().toString(), refreshToken);
        setNewUserAuthorities(savedUser.getId());
        return new JwtAuthorityDto(savedUser.getId(), accessToken, refreshToken);
    }

    public JwtAuthorityDto login(LoginRequestDto loginRequestDto) {
        isLoginExist(loginRequestDto.login());
        User user = getUserIfPasswordCorrect(loginRequestDto.login(), loginRequestDto.password());
        if (user.getDeletedAt() != null) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "User is deleted");
        } else if (user.getBlockedAt() != null) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "User is blocked");
        }
        List<String> roles = authorityService.getUserRoles(user.getId());
        String accessToken = jwtUtils.generateAccessToken(user.getLogin(), user.getId(), roles);
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), user.getId());
        jwtUtils.saveToken(user.getId().toString(), refreshToken);
        return new JwtAuthorityDto(user.getId(), accessToken, refreshToken);
    }

    public void logout(LogoutRequestDto logoutRequestDto) {
        jwtUtils.deleteToken(logoutRequestDto.userId().toString());
    }

    public TokenDto getAccessToken(String refreshToken) {
        checkRefreshToken(refreshToken);
        String login = jwtUtils.getLogin(refreshToken);
        String userId = jwtUtils.getSubject(refreshToken);
        List<String> roles = authorityService.getUserRoles(UUID.fromString(userId));
        return new TokenDto(jwtUtils.generateAccessToken(login, UUID.fromString(userId), roles), null);
    }

    public TokenDto refresh(String refreshToken) {
        checkRefreshToken(refreshToken);
        String login = jwtUtils.getLogin(refreshToken);
        String userId = jwtUtils.getSubject(refreshToken);
        jwtUtils.deleteToken(UUID.fromString(userId).toString());
        String newRefreshToken = jwtUtils.getOrGenerateRefreshToken(login, UUID.fromString(userId));
        jwtUtils.saveToken(userId, newRefreshToken);
        return new TokenDto(null, newRefreshToken);
    }

    private void checkRefreshToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Refresh token is invalid");
        }
        String redisRefreshToken = jwtUtils.getToken(jwtUtils.getSubject(refreshToken));
        if (!refreshToken.equals(redisRefreshToken)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Refresh token is invalid");
        }
    }

    private void setNewUserAuthorities(UUID userId) {
        authorityService.setNewUserAuthorities(userId, Role.BASE_ROLE.getAuthority());
    }

    private User getUserIfPasswordCorrect(String login, String requestPassword) {
        List<User> users = userRepository.findUsersAmongLoginAndEmailByLogin(login)
                .stream()
                .filter(u -> passwordEncoder.matches(requestPassword, u.getPassword()))
                .toList();
        if (users.isEmpty()) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Invalid credentials");
        } else if (users.size() > 1) {
            throw new CustomException(ExceptionType.FATAL, "Database inconsistency");
        }
        return users.get(0);
    }

    private void isLoginExist(String login) {
        if (!userRepository.isProfileExistByLogin(login)) {
            throw new CustomException(ExceptionType.NOT_FOUND, "Login is not found");
        }
    }

    private void isLoginAlreadyUsed(String login) {
        if (userRepository.isProfileExistByLogin(login)) {
            throw new CustomException(ExceptionType.ALREADY_EXISTS, "Login is already used");
        }
    }

    public UserDto createUser(RegistrationRequestDto registrationRequestDto) {
        isLoginAlreadyUsed(registrationRequestDto.login());
        User user = userMapper.map(registrationRequestDto, passwordEncoder.encode(registrationRequestDto.password()));
        userRepository.save(user);
        User savedUser = userRepository.findByLogin(user.getLogin());
        setNewUserAuthorities(savedUser.getId());
        return userMapper.map(savedUser);
    }

    public void updateUser(UUID userId, UpdateUserDto updateUserDto) {
        if (updateUserDto.login() != null) {
            isLoginAlreadyUsed(updateUserDto.login());
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "User is not found"));
        User updatedUser = userMapper.map(user, updateUserDto, passwordEncoder.encode(updateUserDto.password()));
        userRepository.save(updatedUser);
    }

    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "User is not found"));
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public void blockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "User is not found"));
        user.setBlockedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
