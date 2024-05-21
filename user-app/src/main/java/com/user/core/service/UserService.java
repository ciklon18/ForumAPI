package com.user.core.service;

import com.common.auth.annotation.EnableJwtUtils;
import com.common.auth.jwt.Role;
import com.common.auth.util.JwtUtils;
import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.common.kafka.enums.NotificationType;
import com.user.api.dto.*;
import com.user.core.entity.User;
import com.user.core.entity.UserConfirmation;
import com.user.core.mapper.UserMapper;
import com.user.core.repository.UserConfirmationRepository;
import com.user.core.repository.UserRepository;
import com.user.integration.notification.NotificationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@EnableJwtUtils
public class UserService extends BaseUserService {

    private final UserRepository userRepository;
    private final UserConfirmationRepository userConfirmationRepository;
    private final AuthorityService authorityService;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final NotificationService notificationService;

    public UserService(
            UserRepository userRepository,
            UserConfirmationRepository userConfirmationRepository,
            AuthorityService authorityService,
            UserMapper userMapper,
            BCryptPasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            NotificationService notificationService
    ) {
        super(userRepository, authorityService);
        this.userRepository = userRepository;
        this.userConfirmationRepository = userConfirmationRepository;
        this.authorityService = authorityService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.notificationService = notificationService;
    }

    @Transactional
    public RegistrationDto register(RegistrationRequestDto registrationRequestDto) {
        isLoginAndEmailAlreadyUsed(registrationRequestDto.login(), registrationRequestDto.email());
        User user = userMapper.map(registrationRequestDto, passwordEncoder.encode(registrationRequestDto.password()));
        User savedUser = userRepository.save(user);
        setNewUserAuthorities(savedUser.getId());
        UserConfirmation userConfirmation = userConfirmationRepository.save(new UserConfirmation(user.getId()));
        sendConfirmationNotification(savedUser.getEmail(), userConfirmation.getConfirmationCode());
        return new RegistrationDto("User successfully registered");
    }

    private void sendConfirmationNotification(String userEmail, UUID confirmationCode) {
        String confirmationContent = generateConfirmationContent(confirmationCode);
        notificationService.sendNotification(
                "Email confirmation",
                confirmationContent,
                List.of(userEmail),
                NotificationType.MAILING
        );
    }

    private String generateConfirmationContent(UUID confirmationCode) {
        return "http://localhost:9444/api/user/confirm/" + confirmationCode;
    }

    @Transactional
    public JwtAuthorityDto login(LoginRequestDto loginRequestDto) {
        isLoginExist(loginRequestDto.login());
        User user = getUserIfPasswordCorrect(loginRequestDto.login(), loginRequestDto.password());
        if (user.getDeletedAt() != null) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "User is deleted");
        } else if (user.getBlockedAt() != null) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "User is blocked");
        } else  if (!user.isConfirmed()) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "User's email wasn't confirmed");
        }
        List<String> roles = authorityService.getUserRoles(user.getId());
        String accessToken = jwtUtils.generateAccessToken(user.getLogin(), user.getId(), roles);
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), user.getId());
        jwtUtils.saveToken(user.getId().toString(), refreshToken);
        return new JwtAuthorityDto(user.getId(), accessToken, refreshToken);
    }

    @Transactional
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

    @Transactional
    public TokenDto refresh(String refreshToken) {
        checkRefreshToken(refreshToken);
        String login = jwtUtils.getLogin(refreshToken);
        String userId = jwtUtils.getSubject(refreshToken);
        jwtUtils.deleteToken(UUID.fromString(userId).toString());
        String newRefreshToken = jwtUtils.getOrGenerateRefreshToken(login, UUID.fromString(userId));
        jwtUtils.saveToken(userId, newRefreshToken);
        return new TokenDto(null, newRefreshToken);
    }

    @Transactional
    public JwtAuthorityDto confirmAccount(UUID confirmationCode) {
        UserConfirmation userConfirmation = userConfirmationRepository.findByConfirmationCode(confirmationCode)
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Wrong confirmation code"));
        UUID userId = userConfirmation.getUserId();
        userRepository.confirmUser(userId);
        String userLogin = userRepository.getUserLoginById(userId);
        String accessToken = jwtUtils.generateAccessToken(
                userLogin,
                userId,
                List.of(Role.BASE_ROLE.getAuthority())
        );
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(userLogin, userId);
        jwtUtils.saveToken(userId.toString(), refreshToken);
        return new JwtAuthorityDto(userId, accessToken, refreshToken);
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
}
