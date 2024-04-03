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
import com.user.core.entity.UserAuthority;
import com.user.core.mapper.AuthorityMapper;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.AuthorityRepository;
import com.user.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableJwtUtils
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    private final UserMapper userMapper;
    private final AuthorityMapper authorityMapper;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public JwtAuthorityDto register(RegistrationRequestDto registrationRequestDto) {
        isLoginAlreadyUsed(registrationRequestDto.login());
        User user = userMapper.map(registrationRequestDto, UUID.randomUUID(),
                                   passwordEncoder.encode(registrationRequestDto.password()));
        String accessToken =
                jwtUtils.generateAccessToken(user.getLogin(), user.getId(), List.of(Role.BASE_ROLE.getAuthority()));
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), user.getId());
        jwtUtils.saveToken(user.getId().toString(), refreshToken);
        userRepository.save(user);
        setNewUserAuthorities(registrationRequestDto.email());
        return new JwtAuthorityDto(user.getId(), accessToken, refreshToken);
    }

    public JwtAuthorityDto login(LoginRequestDto loginRequestDto) {
        isLoginExist(loginRequestDto.login());
        User user = getUserIfPasswordCorrect(loginRequestDto.login(), loginRequestDto.password());
        List<String> roles = authorityRepository.findAllByUserId(user.getId())
                .stream()
                .map(UserAuthority::getRole)
                .toList();
        String accessToken =
                jwtUtils.generateAccessToken(user.getLogin(), user.getId(), roles);
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
        List<String> roles = authorityRepository.findAllByUserId(user.getId())
                .stream()
                .map(UserAuthority::getRole)
                .toList();
        return jwtUtils.generateAccessToken(user.getLogin(), user.getId(), roles);
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

    private void setNewUserAuthorities(String login) {
        User savedUser = userRepository.findByLogin(login);
        authorityRepository.save(authorityMapper.map(savedUser, Role.BASE_ROLE.getAuthority()));
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


}
