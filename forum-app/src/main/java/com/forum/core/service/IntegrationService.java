package com.forum.core.service;

import com.forum.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final CategoryRepository categoryRepository;

    public boolean isCategoryExist(UUID id) {
        return categoryRepository.existsById(id);
    }
}
