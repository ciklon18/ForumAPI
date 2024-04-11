package com.user.api.rest;

import com.user.api.constant.ApiPaths;
import com.user.api.dto.RegistrationRequestDto;
import com.user.api.dto.UpdateUserDto;
import com.user.api.dto.UserDto;
import com.user.core.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping(ApiPaths.CREATE_USER)
    public UserDto createUser(@RequestBody RegistrationRequestDto registrationRequestDto) {
        return adminService.createUser(registrationRequestDto);
    }

    @PatchMapping(ApiPaths.UPDATE_USER)
    public void updateUser(
            @PathVariable("id") String userId,
            @RequestBody UpdateUserDto updateUserDto
    ) {
        adminService.updateUser(UUID.fromString(userId), updateUserDto);
    }

    @PatchMapping(ApiPaths.DELETE_USER)
    public void deleteUser(@PathVariable("id") String userId) {
        adminService.deleteUser(UUID.fromString(userId));
    }

    @PatchMapping(ApiPaths.BLOCK_USER)
    public void blockUser(@PathVariable("id") String userId) {
        adminService.blockUser(UUID.fromString(userId));
    }
}
