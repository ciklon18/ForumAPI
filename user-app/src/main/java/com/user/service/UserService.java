package com.user.service;

import com.forum.error.ErrorCode;
import com.forum.security.dto.AuthorityType;
import com.forum.security.service.EnableJwtProperty;
import com.forum.security.service.EnableJwtService;
import com.forum.security.service.JwtService;
import com.user.dto.JwtAuthorityDto;
import com.user.dto.LoginRequestDto;
import com.user.dto.RegistrationRequestDto;
import com.user.entity.User;
import com.user.entity.UserAuthority;
import com.user.mapper.AuthorityMapper;
import com.user.mapper.UserMapper;
import com.user.repository.AuthorityRepository;
import com.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@EnableJwtProperty
@EnableJwtService
public class UserService {
    private final AuthorityType BASE_AUTHORITY_TYPE = AuthorityType.USER;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final AuthorityMapper authorityMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public JwtAuthorityDto register(RegistrationRequestDto registrationRequestDto) {
        try {
            UUID userUUID = UUID.randomUUID();
            isEmailAlreadyUsed(registrationRequestDto.email());
            User user = userMapper.map(registrationRequestDto, userUUID,
                                       passwordEncoder.encode(registrationRequestDto.password()));

            String token = jwtService.generateToken(
                    user.getEmail(),
                    Stream.of(BASE_AUTHORITY_TYPE).map(Enum::name).toList(),
                    userUUID
            );
            userRepository.save(user);
            User savedUser = userRepository.findByEmail(user.getEmail());
            authorityRepository.save(authorityMapper.map(savedUser, BASE_AUTHORITY_TYPE.name()));
            return JwtAuthorityDto.builder()
                    .userId(userUUID)
                    .token(token)
                    .authorities(List.of(AuthorityType.USER))
                    .build();
        } catch (Exception e) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    public JwtAuthorityDto login(LoginRequestDto loginRequestDto) {
        isEmailExist(loginRequestDto.email());
        User user = userRepository.findByEmail(loginRequestDto.email());
        isPasswordCorrect(loginRequestDto.password(), user.getPassword());
        List<UserAuthority> userAuthorities = authorityRepository.findAllByUserId(user.getId());
        String token = jwtService.generateToken(
                user.getEmail(),
                userAuthorities.stream().map(UserAuthority::getAuthorityType).toList(),
                user.getId()
        );
        return JwtAuthorityDto.builder()
                .userId(user.getId())
                .token(token)
                .build();
    }

    private void isPasswordCorrect(String requestPassword, String dbPassword) {
        if (!passwordEncoder.matches(requestPassword, dbPassword)) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
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
