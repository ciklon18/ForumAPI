package com.forum.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.forum.api.dto.TopicSubscriptionDto;
import com.forum.api.dto.TopicSubscriptionPaginationDto;
import com.forum.core.entity.TopicSubscription;
import com.forum.core.mapper.TopicSubscriptionMapper;
import com.forum.core.repository.TopicRepository;
import com.forum.core.repository.TopicSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopicSubscriptionService {

    private final TopicRepository topicRepository;
    private final TopicSubscriptionMapper topicSubscriptionMapper;
    private final TopicSubscriptionRepository topicSubscriptionRepository;

    @Transactional
    public void subscribeToTopic(UUID topicId, UUID userId) {
        checkIsTopicExist(topicId);
        checkIsAlreadySubscribed(topicId, userId);
        TopicSubscription topicSubscription = new TopicSubscription(topicId, userId);
        topicSubscriptionRepository.save(topicSubscription);
    }

    @Transactional
    public void unsubscribeFromTopic(UUID topicId, UUID userId) {
        checkIsTopicExist(topicId);
        topicSubscriptionRepository.deleteByTopicIdAndUserId(topicId, userId);
    }

    public TopicSubscriptionPaginationDto getTopicSelections(
            UUID authorId, Integer pageNumber, Integer pageSize
    ) {
        Integer totalPagesAmount = (int) Math.ceil(
                (double) topicSubscriptionRepository.getSubscriptionsCount(authorId) / pageSize);
        pageNumber = pageNumber <= totalPagesAmount ? pageNumber : totalPagesAmount;
        Integer offset = (Math.max((pageNumber - 1), 0)) * pageSize;

        List<TopicSubscriptionDto> topicSubscriptions =
                topicSubscriptionRepository.getSubscriptions(authorId, offset, pageSize)
                        .stream()
                        .map(topicSubscriptionMapper::map)
                        .toList();
        return TopicSubscriptionPaginationDto.builder()
                .topicSubscriptions(topicSubscriptions)
                .pageNumber(pageNumber)
                .totalPagesAmount(totalPagesAmount)
                .build();
    }

    public List<UUID> getSubscribedUserIds(UUID topicId, UUID authorId) {
        return topicSubscriptionRepository.findAllByTopicId(topicId)
                .stream()
                .map(TopicSubscription::getUserId)
                .filter(uuid -> !Objects.equals(uuid, authorId))
                .toList();
    }

    public List<String> getSubscribedUserStringIds(UUID topicId, UUID authorId) {
        return topicSubscriptionRepository.findAllByTopicId(topicId)
                .stream()
                .map(TopicSubscription::getUserId)
                .filter(uuid -> !Objects.equals(uuid, authorId))
                .map(UUID::toString)
                .toList();
    }

    private void checkIsAlreadySubscribed(UUID topicId, UUID userId) {
        if (topicSubscriptionRepository.existsTopicSubscriptionByTopicIdAndUserId(topicId, userId)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "You have already subscribed");
        }
    }

    private void checkIsTopicExist(UUID topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, String.format(
                    "Topic with id=%s doesn't exist.",
                    topicId
            ));
        }
    }
}
