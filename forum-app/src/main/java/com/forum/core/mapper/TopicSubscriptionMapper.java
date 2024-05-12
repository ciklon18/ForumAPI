package com.forum.core.mapper;

import com.forum.api.dto.TopicSubscriptionDto;
import com.forum.core.entity.TopicSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicSubscriptionMapper {
    TopicSubscriptionMapper INSTANCE = Mappers.getMapper(TopicSubscriptionMapper.class);

    TopicSubscriptionDto map(TopicSubscription topicSubscription);
}
