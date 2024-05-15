package com.forum.integration.notification;

import com.common.kafka.annotation.EnableStreamNotificationService;
import com.common.kafka.service.StreamNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@EnableStreamNotificationService
public class NotificationService {
    private final StreamNotificationService streamNotificationService;

    public void sendNotifications(String header, String text, List<UUID> userIds) {
        if (userIds.isEmpty()) return;
        streamNotificationService.sendNotification(header, text, userIds);
    }

    public void sendMessages(String header, String text, List<String> userEmails) {
        if (userEmails.isEmpty()) return;
        streamNotificationService.sendMessage(header, text, userEmails);
    }
}
