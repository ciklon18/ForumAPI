package com.user.mapper;

import com.user.dto.RegistrationRequestDto;
import com.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User map(RegistrationRequestDto registrationRequestDto);
}
