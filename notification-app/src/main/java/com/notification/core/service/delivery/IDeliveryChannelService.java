package com.notification.core.service.delivery;


import com.common.kafka.dto.NotificationDto;

public interface IDeliveryChannelService {
    void sendMessage(NotificationDto notificationDto);
}
