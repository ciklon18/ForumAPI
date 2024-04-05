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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public void assignRole(UUID userId, RoleAssignDto roleAssignDto) {
        if (isCurrentUser(userId)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "You can't change your own role");
        }
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "User not found");
        }
        if (authorityRepository.existsByUserIdAndRole(userId)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "User is already an ADMIN");
        }

        switch (roleAssignDto.role()) {
            case ADMIN -> {
                authorityRepository.save(authorityMapper.map(
                        userId,
                        roleAssignDto.role().getValue()
                ));
                moderatorRepository.deleteAllByUserId(userId);
            }
            case MODERATOR -> {
                if (roleAssignDto.categoryId() == null) {
                    throw new CustomException(ExceptionType.BAD_REQUEST, "Use category id to assign MODERATOR role");
                }
                if (!forumClient.isCategoryExist(roleAssignDto.categoryId())) {
                    throw new CustomException(ExceptionType.BAD_REQUEST, "Category not found");
                }
                if (moderatorRepository.existsByUserId(userId)) {
                    throw new CustomException(ExceptionType.BAD_REQUEST, "User is already a MODERATOR");
                }
                moderatorRepository.save(moderatorMapper.map(
                        userId,
                        roleAssignDto.categoryId()
                ));
            }
            default -> throw new CustomException(ExceptionType.BAD_REQUEST, "User is already an USER");
        }
    }

    public void removeRole(UUID userId) {
        if (isCurrentUser(userId)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "You can't change your own role");
        }
        authorityRepository.deleteAllByUserId(userId);
        moderatorRepository.deleteAllByUserId(userId);
    }

    public boolean isCurrentUser(UUID userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UUID currentUserId) {
            return currentUserId.equals(userId);
        }
        return false;
    }


    public void setNewUserAuthorities(UUID userId, String role) {
        Authority authority = authorityMapper.map(userId, role);
        authorityRepository.save(authority);
    }

    public List<String> getUserRoles(UUID id) {
        List<String> roles = authorityRepository
                .findAllByUserId(id)
                .stream()
                .map(Authority::getRole)
                .sorted(Comparator.reverseOrder())
                .toList();
        if (roles.isEmpty()) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "User's role not found");
        } else {
            return roles;
        }
    }
}
