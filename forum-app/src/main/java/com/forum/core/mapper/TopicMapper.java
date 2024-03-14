package com.forum.core.mapper;

import com.forum.api.dto.TopicCreateDto;
import com.forum.api.dto.TopicDto;
import com.forum.api.dto.TopicUpdateDto;
import com.forum.core.entity.Category;
import com.forum.core.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.UUID;


@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    @Mapping(target = "name", source = "topicCreateDto.name")
    @Mapping(target = "description", source = "topicCreateDto.description")
    @Mapping(target = "category", source = "category")
    Topic map(TopicCreateDto topicCreateDto, Category category, UUID authorId);

    @Mapping(target = "name", source = "topicUpdateDto.name")
    @Mapping(target = "description", source = "topicUpdateDto.description")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Topic map(TopicUpdateDto topicUpdateDto, @MappingTarget Topic topic);

    @Mapping(target = "categoryId", source = "topic.category.id")
    TopicDto map(Topic topic);

}
