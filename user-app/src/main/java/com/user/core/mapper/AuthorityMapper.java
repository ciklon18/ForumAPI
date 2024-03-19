package com.user.core.mapper;

import com.user.core.entity.User;
import com.user.core.entity.UserAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorityMapper {
    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "user", source = "user")
    UserAuthority map(User user, String role);
}
