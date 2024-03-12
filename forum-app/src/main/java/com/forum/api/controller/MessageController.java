package com.forum.api.controller;

import com.forum.api.ApiPaths;
import com.forum.api.dto.MessageCreateDto;
import com.forum.api.dto.MessageDto;
import com.forum.api.dto.MessagePaginationResponse;
import com.forum.api.dto.MessageUpdateDto;
import com.forum.core.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping(ApiPaths.MESSAGE)
    public void createMessage(
            @Valid @RequestBody MessageCreateDto message,
            @RequestParam UUID authorId
    ) {
        messageService.createMessage(message, authorId);
    }

    @PatchMapping(ApiPaths.MESSAGE_BY_ID)
    public void updateMessage(
            @PathVariable UUID messageId,
            @Valid @RequestBody MessageUpdateDto message
    ) {
        messageService.updateMessage(messageId, message);
    }

    @DeleteMapping(ApiPaths.MESSAGE_BY_ID)
    public void deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
    }

    @GetMapping(ApiPaths.MESSAGE_BY_TOPIC_ID)
    public MessagePaginationResponse getMessagesByTopicId(
            @PathVariable UUID topicId,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        pageNumber = pageNumber > 0 ? pageNumber : MessageService.DEFAULT_PAGE_NUMBER;
        pageSize = pageSize > 0 ? pageSize : MessageService.DEFAULT_PAGE_SIZE;
        return messageService.getMessagesByTopic(topicId, pageNumber, pageSize);
    }

    @GetMapping(ApiPaths.MESSAGE)
    public List<MessageDto> getMessages(
            @RequestParam(required = false) String text,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(required = false) LocalDateTime fromDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false) UUID authorId,
            @RequestParam(required = false) UUID topicId,
            @RequestParam(required = false) UUID categoryId
    ) {
        return messageService.getMessages(text, fromDate, toDate, authorId, topicId, categoryId);
    }

    @GetMapping(ApiPaths.MESSAGE_BY_QUERY)
    public List<MessageDto> getMessagesByQuery(@RequestParam(required = false) String text) {
        return messageService.getMessagesByQuery(text);
    }
}
