package com.user.core.service;

import com.common.auth.annotation.EnableJwtUtils;
import com.common.auth.jwt.Role;
import com.common.auth.util.JwtUtils;
import com.common.error.ErrorCode;
import com.user.api.dto.JwtAuthorityDto;
import com.user.api.dto.LoginRequestDto;
import com.user.api.dto.RegistrationRequestDto;
import com.user.core.entity.User;
import com.user.core.entity.UserAuthority;
import com.user.core.mapper.AuthorityMapper;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.AuthorityRepository;
import com.user.core.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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

    @Transactional
    public JwtAuthorityDto register(RegistrationRequestDto registrationRequestDto) {
        try {
            isEmailAlreadyUsed(registrationRequestDto.email());
            User user = userMapper.map(registrationRequestDto, UUID.randomUUID(),
                                       passwordEncoder.encode(registrationRequestDto.password()));
            String token = generateAndSaveToken(user.getLogin(), user.getId());
            userRepository.save(user);
            setNewUserAuthorities(registrationRequestDto.email());
            return new JwtAuthorityDto(user.getId(), token);
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    public JwtAuthorityDto login(LoginRequestDto loginRequestDto) {
        isEmailExist(loginRequestDto.email());
        User user = getUserIfPasswordCorrect(loginRequestDto.password(), loginRequestDto.email());
        String token = generateAndSaveToken(user);
        return new JwtAuthorityDto(user.getId(), token);
    }

    private String generateAndSaveToken(String login, UUID userId) {
        String token =  jwtUtils.generateToken(
                login,
                Stream.of(Role.BASE_ROLE.getAuthority()).toList(),
                userId
        );
        jwtUtils.saveToken(userId.toString(), token);
        return token;
    }

    private void setNewUserAuthorities(String email) {
        User savedUser = userRepository.findByEmail(email);
        authorityRepository.save(authorityMapper.map(savedUser, Role.BASE_ROLE.getAuthority()));
    }

    private String generateAndSaveToken(User user) {
        List<String> roles = authorityRepository.findAllByUserId(user.getId())
                .stream()
                .map(UserAuthority::getRole)
                .toList();
        String token = jwtUtils.generateToken(user.getLogin(), roles, user.getId());
        jwtUtils.saveToken(user.getId().toString(), token);
        return token;
    }

    private User getUserIfPasswordCorrect(String requestPassword, String email) {
        User user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(requestPassword, user.getPassword())) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
        return user;
    }


    private void isEmailExist(String email) {
        if (!userRepository.isProfileExistByEmail(email)) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    private void isEmailAlreadyUsed(String email) {
        if (userRepository.isProfileExistByEmail(email)) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }
}
