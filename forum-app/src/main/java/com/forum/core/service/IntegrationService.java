package com.forum.core.service;

import com.forum.core.repository.CategoryRepository;
import com.forum.core.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final CategoryRepository categoryRepository;
    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    public boolean isCategoryExist(UUID id) {
        return categoryRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean isUserOwnerOfMessage(UUID messageId, UUID userId) {
        return messageRepository.isMessageBelongToUser(messageId, userId);
    }

    @Transactional(readOnly = true)
    public boolean isModerator(UUID messageId, List<UUID> moderatorCategoryIds) {
        if (moderatorCategoryIds.isEmpty()) {
            return false;
        }
        UUID messageCategoryId = messageRepository.getMessageCategoryId(messageId);
        return messageRepository.isModerator(messageCategoryId, moderatorCategoryIds);
    }
}
