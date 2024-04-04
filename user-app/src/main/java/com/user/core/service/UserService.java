package com.user.core.service;

import com.common.auth.annotation.EnableJwtUtils;
import com.common.auth.jwt.Role;
import com.common.auth.util.JwtUtils;
import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.user.api.dto.JwtAuthorityDto;
import com.user.api.dto.LoginRequestDto;
import com.user.api.dto.LogoutRequestDto;
import com.user.api.dto.RegistrationRequestDto;
import com.user.core.entity.User;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = userMapper.map(registrationRequestDto, UUID.randomUUID(),
                                   passwordEncoder.encode(registrationRequestDto.password()));
        String accessToken =
                jwtUtils.generateAccessToken(user.getLogin(), user.getId(), Role.BASE_ROLE.getAuthority());
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), user.getId());
        jwtUtils.saveToken(user.getId().toString(), refreshToken);
        userRepository.save(user);
        setNewUserAuthorities(user.getId());
        return new JwtAuthorityDto(user.getId(), accessToken, refreshToken);
    }

    public JwtAuthorityDto login(LoginRequestDto loginRequestDto) {
        isLoginExist(loginRequestDto.login());
        User user = getUserIfPasswordCorrect(loginRequestDto.login(), loginRequestDto.password());
        String role = authorityService.getUserAuthorities(user.getId());
        String accessToken =
                jwtUtils.generateAccessToken(user.getLogin(), user.getId(), role);
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), user.getId());
        jwtUtils.saveToken(user.getId().toString(), refreshToken);
        return new JwtAuthorityDto(user.getId(), accessToken, refreshToken);
    }

    public void logout(LogoutRequestDto logoutRequestDto) {
        jwtUtils.deleteToken(logoutRequestDto.userId().toString());
    }

    public String getAccessToken(String refreshToken) {
        if (!jwtUtils.checkToken(refreshToken)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Refresh token is invalid");
        }
        String login = jwtUtils.getSubject(refreshToken);
        User user = userRepository.findByLogin(login);
        String role = authorityService.getUserAuthorities(user.getId());
        return jwtUtils.generateAccessToken(user.getLogin(), user.getId(), role);
    }

    public String refresh(String refreshToken) {
        if (!jwtUtils.checkToken(refreshToken)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Refresh token is invalid");
        }
        String login = jwtUtils.getSubject(refreshToken);
        User user = userRepository.findByLogin(login);
        jwtUtils.deleteToken(user.getId().toString());
        String newRefreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), user.getId());
        jwtUtils.saveToken(user.getId().toString(), newRefreshToken);
        return newRefreshToken;
    }

    private void setNewUserAuthorities(UUID userId) {
        authorityService.setNewUserAuthorities(userId, Role.BASE_ROLE.getAuthority());
    }

    private User getUserIfPasswordCorrect(String login, String requestPassword) {
        User user = userRepository.findByLogin(login);
        if (!passwordEncoder.matches(requestPassword, user.getPassword())) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Password is incorrect");
        }
        return user;
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

//    public void createUser(RegistrationRequestDto registrationRequestDto) {
//    }
//
//    public void updateUser(UpdateUserRequestDto updateUserRequestDto) {
//    }
//
//    public void deleteUser(String userId) {
//    }
//
//    public void blockUser(String userId) {
//    }
}
