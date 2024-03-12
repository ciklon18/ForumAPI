package com.forum.core.mapper;

import com.forum.api.dto.CategoryCreateDto;
import com.forum.api.dto.CategoryDto;
import com.forum.api.dto.CategoryUpdateDto;
import com.forum.core.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper
public interface CategoryMapper {
    Category map(CategoryCreateDto categoryCreateDto, UUID authorId);

    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    Category map(CategoryUpdateDto categoryUpdateDto, @MappingTarget Category category);

    @Mapping(target = "childCategories", source = "childCategories")
    CategoryDto map(Category category);
}
