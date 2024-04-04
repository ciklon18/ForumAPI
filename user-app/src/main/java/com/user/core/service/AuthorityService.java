package com.user.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.user.api.dto.RoleAssignDto;
import com.user.core.entity.Authority;
import com.user.core.mapper.AuthorityMapper;
import com.user.core.mapper.ModeratorMapper;
import com.user.core.repository.AuthorityRepository;
import com.user.core.repository.ModeratorRepository;
import com.user.core.repository.UserRepository;
import com.user.integration.client.forum.ForumClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final ModeratorRepository moderatorRepository;
    private final UserRepository userRepository;
    private final ForumClient forumClient;
    private final AuthorityMapper authorityMapper;
    private final ModeratorMapper moderatorMapper;

    public void assignRole(RoleAssignDto roleAssignDto) {
        if (!userRepository.existsById(roleAssignDto.userId())) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "User not found");
        }

        if (roleAssignDto.categoryId() != null) {
            if (!forumClient.isCategoryExist(roleAssignDto.categoryId())) {
                throw new CustomException(ExceptionType.BAD_REQUEST, "Category not found");
            }
            moderatorRepository.save(moderatorMapper.map(
                    roleAssignDto.userId(),
                    roleAssignDto.categoryId()
            ));
        } else {
            authorityRepository.save(authorityMapper.map(
                    roleAssignDto.userId(),
                    roleAssignDto.role()
            ));
        }
    }

    public void removeRole(UUID userId) {
        authorityRepository.deleteAllByUserId(userId);
        moderatorRepository.deleteAllByUserId(userId);
    }

    public void setNewUserAuthorities(UUID userId, String role) {
        authorityRepository.save(authorityMapper.map(userId, role));
    }

    public String getUserAuthorities(UUID id) {
        List<String> roles = authorityRepository
                .findAllByUserId(id)
                .stream()
                .map(Authority::getRole)
                .sorted(Comparator.reverseOrder())
                .toList();
        if (roles.isEmpty()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "User's role not found");
        } else {
            return roles.get(0);
        }
    }
}
