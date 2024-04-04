package com.user.api.controller;

import com.user.api.constant.ApiPaths;
import com.user.api.dto.RoleAssignDto;
import com.user.core.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    @PatchMapping(ApiPaths.ASSIGN_ROLE)
    public void assignRole(@RequestBody RoleAssignDto roleAssignDto) {
        authorityService.assignRole(roleAssignDto);
    }

    @PatchMapping(ApiPaths.REMOVE_ROLE)
    public void removeRole(@RequestBody UUID userId) {
        authorityService.removeRole(userId);
    }
}
