package com.notification.core.mapper;

import com.common.kafka.dto.NotificationDto;
import com.notification.api.dto.UserNotificationDto;
import com.notification.core.entity.NotificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationEntity map(NotificationDto notificationDto);

    @Mapping(source = "notificationStatus", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    UserNotificationDto map(NotificationEntity notificationEntity);
}
