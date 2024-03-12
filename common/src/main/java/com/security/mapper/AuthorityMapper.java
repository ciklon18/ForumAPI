package com.user.mapper;

import com.user.dto.AuthorityResponse;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AuthorityMapper {
    AuthorityResponse map(UUID userId, List<String> authorities);
}
