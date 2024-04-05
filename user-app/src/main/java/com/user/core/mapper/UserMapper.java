package com.user.core.mapper;

import com.user.api.dto.RegistrationRequestDto;
import com.user.api.dto.UpdateUserDto;
import com.user.api.dto.UserDto;
import com.user.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "password", source = "encode")
    User map(RegistrationRequestDto registrationRequestDto, String encode);

    UserDto map(User user);

    @Mapping(target = "password", source = "encode", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "login", source = "updateUserDto.login", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", source = "updateUserDto.email", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "name", source = "updateUserDto.name", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "surname", source = "updateUserDto.surname", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phone", source = "updateUserDto.phone", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    User map(@MappingTarget User user, UpdateUserDto updateUserDto, String encode);
}
