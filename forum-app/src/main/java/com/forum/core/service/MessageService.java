package com.forum.core.service;

import com.forum.api.dto.MessageCreateDto;
import com.forum.api.dto.MessageDto;
import com.forum.api.dto.MessagePaginationResponse;
import com.forum.api.dto.MessageUpdateDto;
import com.forum.core.entity.Message;
import com.forum.core.entity.Topic;
import com.forum.core.mapper.MessageMapper;
import com.forum.core.repository.MessageRepository;
import com.forum.core.repository.TopicRepository;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MessageService {
    public static final Integer DEFAULT_PAGE_NUMBER = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;

    private final MessageRepository messageRepository;
    private final TopicRepository topicRepository;
    private final MessageMapper messageMapper;

    public void createMessage(MessageCreateDto messageCreateDto, UUID authorId) {
        Topic topic = topicRepository.findById(messageCreateDto.topicId())
                .orElseThrow(() -> new RuntimeException("Topic not found"));
        Message message = messageMapper.map(messageCreateDto, authorId, topic);

        messageRepository.save(message);
    }

    public void updateMessage(UUID messageId, MessageUpdateDto messageUpdateDto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        Message updatedMessage = messageMapper.map(messageUpdateDto, message);
        messageRepository.save(updatedMessage);
    }

    public void deleteMessage(UUID messageId) {
        messageRepository.deleteById(messageId);
    }

    public MessagePaginationResponse getMessagesByTopic(UUID topicId, Integer pageNumber, Integer pageSize) {
        isTopicExist(topicId);
        Integer totalPagesAmount = (int) Math.ceil((double) messageRepository.getMessagesCount(topicId) / pageSize);

        pageNumber = pageNumber <= totalPagesAmount ? pageNumber : totalPagesAmount;
        Integer offset = (Math.max((pageNumber - 1), 0)) * pageSize;
        return MessagePaginationResponse.builder()
                .messages(messageRepository.getMessagesByTopic(topicId, offset, pageSize)
                                  .stream()
                                  .map(messageMapper::map)
                                  .toList())
                .pageNumber(pageNumber)
                .totalPagesAmount(totalPagesAmount)
                .build();
    }

    private void isTopicExist(UUID topicId) {
        topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));
    }

    public List<MessageDto> getMessages(
            String text,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            UUID authorId,
            UUID topicId,
            UUID categoryId
    ) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new ServiceException(
                    String.format("fromDate \"%s\" должна быть раньше fromDate \"%s\"", fromDate, toDate)
            );
        }

        return messageRepository.getFilteredMessages(text, fromDate, toDate, authorId, topicId,
                                                                          categoryId)
                .stream()
                .map(messageMapper::map)
                .toList();
    }

    public List<MessageDto> getMessagesByQuery(String text) {
        return messageRepository.getMessagesByQuery(text)
                .stream()
                .map(messageMapper::map)
                .toList();
    }
}
