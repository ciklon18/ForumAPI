package com.notification.core.service;

import com.common.kafka.dto.MessageDto;
import com.common.kafka.dto.NotificationDto;
import com.notification.core.service.delivery.IDeliveryChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationStreamService {
    private final NotificationService notificationService;
    private final IDeliveryChannelService deliveryChannelService;

    public void saveNotificationsToHistory(NotificationDto notificationDto) {
        notificationService.saveNotificationsToHistory(notificationDto);
    }

    public void sendMessageToEmail(MessageDto messageDto) {
        deliveryChannelService.sendMessage(messageDto);
    }
}
