package com.forum.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.forum.api.dto.TopicCreateDto;
import com.forum.api.dto.TopicDto;
import com.forum.api.dto.TopicPaginationResponse;
import com.forum.api.dto.TopicUpdateDto;
import com.forum.core.entity.Category;
import com.forum.core.entity.Topic;
import com.forum.core.entity.TopicSubscription;
import com.forum.core.mapper.TopicMapper;
import com.forum.core.repository.CategoryRepository;
import com.forum.core.repository.TopicRepository;
import com.forum.core.repository.TopicSubscriptionRepository;
import com.forum.integration.user.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TopicService {

    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer DEFAULT_PAGE_NUMBER = 1;

    private final TopicRepository topicRepository;
    private final TopicSubscriptionRepository topicSubscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final TopicMapper topicMapper;
    private final UserClient userClient;

    @Transactional
    public UUID createTopic(TopicCreateDto topicCreateDto, UUID authorId) {
        if (!userClient.isUserExist(authorId)){
            throw new CustomException(ExceptionType.BAD_REQUEST, "User not found");
        }
        Category category = categoryRepository.getCategoryIfLastLevel(topicCreateDto.categoryId())
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Wrong category id"));
        isTopicNameOriginal(topicCreateDto.name());
        Topic topic = topicMapper.map(topicCreateDto, category, authorId);
        topicRepository.save(topic);
        return topic.getId();
    }

    @Transactional
    public void updateTopic(UUID topicId, TopicUpdateDto topicUpdateDto) {
        isTopicNameOriginal(topicUpdateDto.name());
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Topic not found"));
        Topic updatedTopic = topicMapper.map(topicUpdateDto, topic);
        topicRepository.save(updatedTopic);
    }

    @Transactional
    public void deleteTopic(UUID topicId) {
        topicRepository.deleteById(topicId);
    }

    @Transactional(readOnly = true)
    public TopicPaginationResponse getTopics(Integer pageNumber, Integer pageSize) {
        Integer totalPagesAmount = (int) Math.ceil((double) topicRepository.getTopicsCount() / pageSize);
        pageNumber = pageNumber <= totalPagesAmount ? pageNumber : totalPagesAmount;
        Integer offset = (Math.max((pageNumber - 1), 0)) * pageSize;
        return TopicPaginationResponse.builder()
                .topics(topicRepository.getTopics(offset, pageSize).stream().map(topicMapper::map).toList())
                .pageNumber(pageNumber)
                .totalPagesAmount(totalPagesAmount)
                .build();
    }

    @Transactional(readOnly = true)
    public List<TopicDto> getTopicsByQuery(String query) {
        return topicRepository.getTopicsByQuery(query).stream().map(topicMapper::map).toList();
    }

    @Transactional
    public void subscribeToTopic(UUID topicId, UUID userId) {
        checkIsTopicExist(topicId);
        checkIsAlreadySubscribed(topicId, userId);
        TopicSubscription topicSubscription = new TopicSubscription(topicId, userId);
        topicSubscriptionRepository.save(topicSubscription);
    }

    private void checkIsAlreadySubscribed(UUID topicId, UUID userId) {
        if (topicSubscriptionRepository.existsTopicSubscriptionByTopicIdAndUserId(topicId, userId)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "You have already subscribed");
        }
    }

    @Transactional
    public void unsubscribeFromTopic(UUID topicId, UUID userId) {
        checkIsTopicExist(topicId);
        topicSubscriptionRepository.deleteByTopicIdAndUserId(topicId, userId);
    }

    private void isTopicNameOriginal(String name) {
        topicRepository.findByName(name).ifPresent(topic -> {
            throw new CustomException(
                    ExceptionType.ALREADY_EXISTS,
                    "Topic with this name already exists"
            );
        });
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
