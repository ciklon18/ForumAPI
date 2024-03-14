package com.user.controller;

import com.forum.error.ErrorCode;
import com.user.constants.ApiPaths;
import com.user.dto.JwtAuthorityDto;
import com.user.dto.LoginRequestDto;
import com.user.dto.RegistrationRequestDto;
import com.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(ApiPaths.REGISTER)
    public JwtAuthorityDto register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        try {
            return userService.register(registrationRequestDto);
        } catch (Exception e){
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }

    @PostMapping(ApiPaths.LOGIN)
    public JwtAuthorityDto login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            return userService.login(loginRequestDto);
        } catch (Exception e){
            throw new ServiceException(ErrorCode.INTERNAL_ERROR.getCode());
        }
    }
}
