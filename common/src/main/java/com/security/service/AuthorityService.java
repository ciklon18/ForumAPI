package com.user.service;

import com.user.dto.AuthorityResponse;
import com.user.dto.AuthorityType;
import com.user.entity.UserAuthority;
import com.user.mapper.AuthorityMapper;
import com.user.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;

    public AuthorityResponse getAuthorities(UUID userId) {
        return AuthorityResponse.builder()
                .userId(userId)
                .authorities(authorityRepository.findAllByUserId(userId)
                                     .stream()
                                     .map(UserAuthority::getAuthorityType)
                                     .map(AuthorityType::valueOf)
                                     .toList())
                .build();
    }
}
