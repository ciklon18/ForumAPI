package com.user.controller;

import com.user.constants.ApiPaths;
import com.user.dto.AuthorityResponse;
import com.user.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @GetMapping(ApiPaths.AUTHORITY)
    public AuthorityResponse getAuthorities(@RequestParam UUID userId) {
        return authorityService.getAuthorities(userId);
    }
}
