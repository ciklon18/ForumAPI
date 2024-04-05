package com.user.api.controller;

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
    public TokenDto refresh(@RequestBody TokenDto tokenDto) {
        return userService.refresh(tokenDto.refreshToken());
    }

    @PostMapping(ApiPaths.ACCESS)
    public TokenDto getAccessToken(@RequestBody TokenDto tokenDto) {
        return userService.getAccessToken(tokenDto.refreshToken());
    }

    @PostMapping(ApiPaths.CREATE_USER)
    public UserDto createUser(@RequestBody RegistrationRequestDto registrationRequestDto) {
        return userService.createUser(registrationRequestDto);
    }

    @PatchMapping(ApiPaths.UPDATE_USER)
    public void updateUser(
            @PathVariable("id") String userId,
            @RequestBody UpdateUserDto updateUserDto
    ) {
        userService.updateUser(UUID.fromString(userId), updateUserDto);
    }

    @PatchMapping(ApiPaths.DELETE_USER)
    public void deleteUser(@PathVariable("id") String userId) {
        userService.deleteUser(UUID.fromString(userId));
    }

    @PatchMapping(ApiPaths.BLOCK_USER)
    public void blockUser(@PathVariable("id") String userId) {
        userService.blockUser(UUID.fromString(userId));
    }
}
