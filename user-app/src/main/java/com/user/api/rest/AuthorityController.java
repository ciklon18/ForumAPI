package com.user.api.rest;

import com.user.api.constant.ApiPaths;
import com.user.api.dto.RoleAssignDto;
import com.user.core.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorityController {

    private final AuthorityService authorityService;

    @PostMapping(ApiPaths.ASSIGN_ROLE)
    public void assignRole(@PathVariable("id") UUID userId, @RequestBody RoleAssignDto roleAssignDto) {
        authorityService.assignRole(userId, roleAssignDto);
    }

    @DeleteMapping(ApiPaths.REMOVE_ROLE)
    public void removeRole(@PathVariable("id") UUID userId) {
        authorityService.removeRole(userId);
    }
}
