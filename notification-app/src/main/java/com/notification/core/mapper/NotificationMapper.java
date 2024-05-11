package com.notification.core.mapper;

import com.common.notification.dto.NotificationDto;
import com.notification.core.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationEntity map(NotificationDto notificationDto);
}
