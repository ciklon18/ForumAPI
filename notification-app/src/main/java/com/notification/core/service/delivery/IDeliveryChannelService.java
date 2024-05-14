package com.notification.core.service.delivery;

import com.common.notification.dto.MessageDto;

public interface IDeliveryChannelService {
    void sendMessage(MessageDto messageDto);
}
