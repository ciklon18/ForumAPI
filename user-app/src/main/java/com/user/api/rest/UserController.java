package com.user.api.rest;

import com.user.api.constant.ApiPaths;
import com.user.api.dto.*;
import com.user.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(ApiPaths.REGISTER)
    public RegistrationDto register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        return userService.register(registrationRequestDto);
    }

    @PostMapping(ApiPaths.LOGIN)
    public JwtAuthorityDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto);
    }

    @PostMapping(ApiPaths.LOGOUT)
    public void logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        userService.logout(logoutRequestDto);
    }

    @PostMapping(ApiPaths.REFRESH)
    public TokenDto refresh(@RequestBody TokenDto tokenDto) {
        return userService.refresh(tokenDto.refreshToken());
    }

    @PostMapping(ApiPaths.ACCESS)
    public TokenDto getAccessToken(@RequestBody TokenDto tokenDto) {
        return userService.getAccessToken(tokenDto.refreshToken());
    }

    @GetMapping(ApiPaths.CONFIRMATION)
    public JwtAuthorityDto confirmAccount(@PathVariable("confirmation_code") UUID confirmation_code) {
        return userService.confirmAccount(confirmation_code);
    }
}
