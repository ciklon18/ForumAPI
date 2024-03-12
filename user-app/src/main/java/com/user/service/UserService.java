package com.user.service;

import com.user.dto.AuthorityType;
import com.user.dto.JwtAuthorityDto;
import com.user.dto.LoginRequestDto;
import com.user.dto.RegistrationRequestDto;
import com.user.entity.User;
import com.user.entity.UserAuthority;
import com.user.error.ErrorCode;
import com.user.mapper.UserMapper;
import com.user.repository.AuthorityRepository;
import com.user.repository.UserRepository;
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
public class UserService {

    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JwtAuthorityDto register(RegistrationRequestDto registrationRequestDto) {
        try {
            UUID uuid = UUID.randomUUID();
            isEmailAlreadyUsed(registrationRequestDto.email());
            User user = userMapper.map(registrationRequestDto);
            user.setId(uuid);
            user.setPassword(passwordEncoder.encode(registrationRequestDto.password()));

            String token = jwtService.generateToken(
                    user.getEmail(),
                    Stream.of(AuthorityType.USER).map(Enum::name).toList(),
                    uuid
            );
            userRepository.save(user);
//            UserAuthority userAuthority =
//                    UserAuthority.builder()
//                            .userId(uuid)
//                            .authorityType(AuthorityType.USER.name())
//                            .build();
//            authorityRepository.save(userAuthority);
            return JwtAuthorityDto.builder()
                    .userId(uuid)
                    .token(token)
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
