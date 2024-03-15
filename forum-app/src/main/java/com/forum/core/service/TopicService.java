package com.forum.core.service;

import com.forum.api.dto.TopicCreateDto;
import com.forum.api.dto.TopicDto;
import com.forum.api.dto.TopicPaginationResponse;
import com.forum.api.dto.TopicUpdateDto;
import com.forum.core.entity.Category;
import com.forum.core.entity.Topic;
import com.forum.core.mapper.TopicMapper;
import com.forum.core.repository.CategoryRepository;
import com.forum.core.repository.TopicRepository;
import com.forum.integration.user.UserClient;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TopicService {
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer DEFAULT_PAGE_NUMBER = 1;

    private final TopicRepository topicRepository;
    private final CategoryRepository categoryRepository;
    private final TopicMapper topicMapper;
    private final UserClient userClient;
    public UUID createTopic(TopicCreateDto topicCreateDto, UUID authorId) throws BadRequestException {
        if (!userClient.checkUserExisingById(authorId)){
            throw new BadRequestException("Author not found");
        }
        Category category = categoryRepository.getCategoryIfLastLevel(topicCreateDto.categoryId());
        if (category == null) {
            throw new IllegalArgumentException("Wrong category id");
        }
        isTopicNameOriginal(topicCreateDto.name());
        Topic topic = topicMapper.map(topicCreateDto, category, authorId);
        topicRepository.save(topic);
        return topic.getId();
    }

    public void updateTopic(UUID topicId, TopicUpdateDto topicUpdateDto) {
        isTopicNameOriginal(topicUpdateDto.name());
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new RuntimeException("Topic not found"));
        Topic updatedTopic = topicMapper.map(topicUpdateDto, topic);
        topicRepository.save(updatedTopic);
    }

    public void deleteTopic(UUID topicId) {
        topicRepository.deleteById(topicId);
    }

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

    public List<TopicDto> getTopicsByQuery(String query) {
        return topicRepository.getTopicsByQuery(query).stream().map(topicMapper::map).toList();
    }


    private void isTopicNameOriginal(String name) {
        topicRepository.findByName(name).ifPresent(topic -> {
            throw new IllegalArgumentException("Topic with name " + name + " already exists");
        });
    }
}
