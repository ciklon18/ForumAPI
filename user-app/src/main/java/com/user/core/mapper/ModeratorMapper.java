package com.user.core.mapper;


import com.user.core.entity.Moderator;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface ModeratorMapper {
    ModeratorMapper INSTANCE = Mappers.getMapper(ModeratorMapper.class);

    Moderator map(UUID userId, UUID categoryId);
}
