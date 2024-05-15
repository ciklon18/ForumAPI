package com.notification.core.service.delivery;

import com.common.kafka.dto.MessageDto;

public interface IDeliveryChannelService {
    void sendMessage(MessageDto messageDto);
}
