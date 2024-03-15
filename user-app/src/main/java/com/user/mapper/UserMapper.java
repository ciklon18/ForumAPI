package com.user.mapper;

import com.user.dto.RegistrationRequestDto;
import com.user.dto.UserDto;
import com.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "id", source = "uuid")
    @Mapping(target = "password", source = "encode")
    User map(RegistrationRequestDto registrationRequestDto, UUID uuid, String encode);

    UserDto map(User user);
}
