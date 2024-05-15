package com.user.integration.notification;

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

    public void sendNotification(String header, String text, List<UUID> userIds) {
        streamNotificationService.sendNotification(header, text, userIds);
    }

    public void sendMessage(String header, String text, List<String> userEmails) {
        streamNotificationService.sendMessage(header, text, userEmails);
    }
}
