package com.user.controller;

import com.forum.security.constants.ApiPaths;
import com.user.dto.UserDto;
import com.user.mapper.UserMapper;
import com.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class IntegrationController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping(ApiPaths.CHECK_USER_BY_ID)
    public Boolean checkUserExisingById(@PathVariable("id") UUID userId) {
        return userRepository.existsById(userId);
    }

    @GetMapping(ApiPaths.USER_BY_ID)
    public UserDto getUserById(@PathVariable("id") UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::map)
                .orElse(null);
    }
}
