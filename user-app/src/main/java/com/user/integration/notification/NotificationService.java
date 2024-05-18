package com.user.integration.notification;

import com.common.kafka.annotation.EnableStreamNotificationService;
import com.common.kafka.enums.NotificationType;
import com.common.kafka.service.StreamNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableStreamNotificationService
public class NotificationService {
    private final StreamNotificationService streamNotificationService;

    public void sendNotification(String header, String text, List<String> userData, NotificationType type) {
        streamNotificationService.sendNotification(header, text, userData, type);
    }
}
