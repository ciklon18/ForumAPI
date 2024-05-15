package com.forum.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.forum.api.dto.MessageCreateDto;
import com.forum.api.dto.MessageDto;
import com.forum.api.dto.MessagePaginationResponse;
import com.forum.api.dto.MessageUpdateDto;
import com.forum.core.entity.Message;
import com.forum.core.entity.Topic;
import com.forum.core.mapper.MessageMapper;
import com.forum.core.repository.MessageRepository;
import com.forum.core.repository.TopicRepository;
import com.forum.integration.notification.NotificationService;
import com.forum.integration.user.UserClient;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
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
    private final UserClient userClient;

    private final NotificationService notificationService;
    private final TopicSubscriptionService topicSubscriptionService;

    @Transactional
    public void createMessage(MessageCreateDto messageCreateDto, UUID authorId) {
        if (!userClient.isUserExist(authorId)){
            throw new CustomException(ExceptionType.BAD_REQUEST, "User not found");
        }
        Topic topic = topicRepository.findById(messageCreateDto.topicId())
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Topic not found"));
        Message message = messageMapper.map(messageCreateDto, authorId, topic);

        messageRepository.save(message);

        sendNotificationToUsers(topic.getId(), topic.getName(), authorId);
        sendMessageToUsers(topic.getId(), topic.getName(), authorId);
    }

    @Transactional
    public void updateMessage(UUID messageId, MessageUpdateDto messageUpdateDto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Message not found"));
        Message updatedMessage = messageMapper.map(messageUpdateDto, message);
        messageRepository.save(updatedMessage);
    }

    @Transactional
    public void deleteMessage(UUID messageId) {
        messageRepository.deleteById(messageId);
    }

    @Transactional(readOnly = true)
    public MessagePaginationResponse getMessagesByTopic(UUID topicId, Integer pageNumber, Integer pageSize) {
        isTopicExist(topicId);

        Integer totalPagesAmount = (int) Math.ceil((double) messageRepository.getMessagesCount(topicId) / pageSize);
        pageNumber = pageNumber <= totalPagesAmount ? pageNumber : totalPagesAmount;
        Integer offset = (Math.max((pageNumber - 1), 0)) * pageSize;

        List<MessageDto> messages = messageRepository.getMessagesByTopic(topicId, offset, pageSize)
                .stream()
                .map(messageMapper::map)
                .toList();
        return MessagePaginationResponse.builder()
                .messages(messages)
                .pageNumber(pageNumber)
                .totalPagesAmount(totalPagesAmount)
                .build();
    }

    @Transactional(readOnly = true)
    public List<MessageDto> getMessages(
            String text,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            UUID authorId,
            UUID topicId,
            UUID categoryId
    ) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new CustomException(ExceptionType.BAD_REQUEST, "From date should be before to date");
        }

        return messageRepository.getFilteredMessages(text, fromDate, toDate, authorId, topicId,
                                                                          categoryId)
                .stream()
                .map(messageMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MessageDto> getMessagesByQuery(String text) {
        return messageRepository.getMessagesByQuery(text)
                .stream()
                .map(messageMapper::map)
                .toList();
    }

    private void isTopicExist(UUID topicId) {
        topicRepository.findById(topicId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Topic not found"));
    }

    private void sendNotificationToUsers(UUID topicId, String topicName, UUID authorId) {
        List<UUID> subscribedUserIds = topicSubscriptionService.getSubscribedUserIds(topicId, authorId);
        notificationService.sendNotifications(
                "New message in topic" + topicName,
                "User with id=" + authorId + " wrote new message.",
                subscribedUserIds
        );
    }

    private void sendMessageToUsers(UUID topicId, String topicName, UUID authorId) {
        List<String> subscribedUserEmails = getUserEmails(topicId, authorId);
        notificationService.sendMessages(
                "New message in topic" + topicName,
                "User with id=" + authorId + " wrote new message.",
                subscribedUserEmails
        );
    }

    private List<String> getUserEmails(UUID topicId, UUID authorId){
        List<UUID> subscribedUserIds = topicSubscriptionService.getSubscribedUserIds(topicId, authorId);
        if (subscribedUserIds.isEmpty()) return Collections.emptyList();
        return userClient.getUserEmails(subscribedUserIds);
    }
}
