package com.user.api.controller;

import com.user.api.constant.IntegrationPaths;
import com.user.api.dto.UserDto;
import com.user.core.service.IntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IntegrationController {

    private final IntegrationService integrationService;

    @GetMapping(IntegrationPaths.CHECK_USER_BY_ID)
    public Boolean isUserExist(@PathVariable("id") UUID userId) {
        return integrationService.isUserExist(userId);
    }

    @GetMapping(IntegrationPaths.USER_BY_ID)
    public UserDto getUserById(@PathVariable("id") UUID userId) {
        return integrationService.getUserById(userId);
    }
}
