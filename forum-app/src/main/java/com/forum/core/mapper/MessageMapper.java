package com.forum.core.mapper;

import com.forum.api.dto.MessageCreateDto;
import com.forum.api.dto.MessageDto;
import com.forum.api.dto.MessageUpdateDto;
import com.forum.core.entity.Message;
import com.forum.core.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(target = "text", source = "messageCreateDto.text")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "authorId", source = "authorId")
    Message map(MessageCreateDto messageCreateDto, UUID authorId, Topic topic);

    @Mapping(target = "text", source = "messageUpdateDto.text")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Message map(MessageUpdateDto messageUpdateDto, @MappingTarget Message message);
    MessageDto map(Message message);}
