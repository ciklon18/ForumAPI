package com.user.api.controller;

import com.user.api.constant.ApiPaths;
import com.user.api.dto.JwtAuthorityDto;
import com.user.api.dto.LoginRequestDto;
import com.user.api.dto.LogoutRequestDto;
import com.user.api.dto.RegistrationRequestDto;
import com.user.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(ApiPaths.REGISTER)
    public JwtAuthorityDto register(@RequestBody RegistrationRequestDto registrationRequestDto) {
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
    public String refresh(@RequestParam String refreshToken) {
        return userService.refresh(refreshToken);
    }

    @PostMapping(ApiPaths.ACCESS)
    public String getAccessToken(@RequestParam String refreshToken) {
        return userService.getAccessToken(refreshToken);
    }
}
