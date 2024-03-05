package com.user.mapper;

import com.user.dto.RegistrationRequestDto;
import com.user.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    Profile map(RegistrationRequestDto registrationRequestDto);
}
