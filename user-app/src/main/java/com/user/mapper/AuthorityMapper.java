package com.user.mapper;

import com.user.dto.AuthorityResponse;
import com.user.entity.User;
import com.user.entity.UserAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AuthorityMapper {
    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    AuthorityResponse map(UUID userId, List<String> authorities);

    @Mapping(target = "authorityType", source = "authorityType")
    @Mapping(target = "user", source = "user")
    UserAuthority map(User user, String authorityType);
}
