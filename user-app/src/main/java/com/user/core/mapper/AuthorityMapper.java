package com.user.core.mapper;

import com.user.core.entity.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;


@Mapper
public interface AuthorityMapper {
    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "userId", source = "userId")
    Authority map(UUID userId, String role);
}
