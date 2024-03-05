package com.user.service;

import com.user.dto.AuthorityType;
import com.user.dto.JwtAuthorityDto;
import com.user.dto.LoginRequestDto;
import com.user.dto.RegistrationRequestDto;
import com.user.entity.Profile;
import com.user.entity.UserAuthority;
import com.user.error.ErrorCode;
import com.user.mapper.UserMapper;
import com.user.repository.AuthorityRepository;
import com.user.repository.ProfileRepository;
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

    private final ProfileRepository profileRepository;
    private final AuthorityRepository authorityRepository;

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JwtAuthorityDto register(RegistrationRequestDto registrationRequestDto) {
        try {
            UUID uuid = UUID.randomUUID();
            isEmailAlreadyUsed(registrationRequestDto.email());
            Profile profile = userMapper.map(registrationRequestDto);
            profile.setId(uuid);
            profile.setPassword(passwordEncoder.encode(registrationRequestDto.password()));

            String token = jwtService.generateToken(
                    profile.getEmail(),
                    Stream.of(AuthorityType.USER).map(Enum::name).toList(),
                    uuid
            );
            profileRepository.save(profile);
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
        Profile profile = profileRepository.findByEmail(loginRequestDto.email());
        isPasswordCorrect(loginRequestDto.password(), profile.getPassword());
        List<UserAuthority> userAuthorities = authorityRepository.findAllByUserId(profile.getId());
        String token = jwtService.generateToken(
                profile.getEmail(),
                userAuthorities.stream().map(UserAuthority::getAuthorityType).toList(),
                profile.getId()
        );
        return JwtAuthorityDto.builder()
                .userId(profile.getId())
                .token(token)
                .build();
    }

    private void isPasswordCorrect(String requestPassword, String dbPassword) {
        if (!passwordEncoder.matches(requestPassword, dbPassword)) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    private void isEmailExist(String email) {
        if (!profileRepository.isProfileExistByEmail(email)) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    private void isEmailAlreadyUsed(String email) {
        if (profileRepository.isProfileExistByEmail(email)) {
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }
}
