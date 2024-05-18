package com.notification.core.service;

import com.common.kafka.dto.NotificationDto;
import com.notification.core.service.delivery.IDeliveryChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationStreamService {
    private final NotificationService notificationService;
    private final IDeliveryChannelService deliveryChannelService;

    public void handleNotification(NotificationDto notificationDto) {
        switch (notificationDto.notificationType()){
            case MAILING -> deliveryChannelService.sendMessage(notificationDto);
            case HISTORY -> notificationService.saveNotificationsToHistory(notificationDto);
            case ALL -> {
                notificationService.saveNotificationsToHistory(notificationDto);
                deliveryChannelService.sendMessage(notificationDto);
            }
        }
    }
}
