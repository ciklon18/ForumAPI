package com.forum.core.service;

import com.common.exception.CustomException;
import com.common.exception.ExceptionType;
import com.forum.api.dto.CategoryCreateDto;
import com.forum.api.dto.CategoryDto;
import com.forum.api.dto.CategoryUpdateDto;
import com.forum.core.entity.Category;
import com.forum.core.mapper.CategoryMapper;
import com.forum.core.repository.CategoryRepository;
import com.forum.integration.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserClient userClient;

    @Transactional
    public UUID createCategory(CategoryCreateDto categoryCreateDto, UUID authorId)  {
        isCategoryNameOriginal(categoryCreateDto.name());
        if (!userClient.isUserExist(authorId)){
            throw new CustomException(ExceptionType.BAD_REQUEST, "User not found");
        }
        Category category = categoryMapper.map(categoryCreateDto, authorId);
        updateParentCategory(category, categoryCreateDto.parentId());
        categoryRepository.save(category);
        return category.getId();
    }

    @Transactional
    public void updateCategory(UUID categoryId, CategoryUpdateDto categoryUpdateDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ExceptionType.NOT_FOUND, "Category not found"));
        if (!category.getName().equals(categoryUpdateDto.name())){
            isCategoryNameOriginal(categoryUpdateDto.name());
        }
        Category updatedCategory = categoryMapper.map(categoryUpdateDto, category);
        categoryRepository.save(updatedCategory);
    }

    @Transactional
    public void deleteCategoryHierarchyById(UUID categoryId) {
        isCategoryExist(categoryId);
        List<Category> categories = categoryRepository.getCategoriesHierarchyById(categoryId);
        categoryRepository.deleteAll(categories);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryHierarchy() {
        return categoryRepository.getCategoryHierarchy()
                .stream()
                .sorted(Comparator.comparing(Category::getName))
                .filter(category -> category.getParentCategory() == null)
                .map(categoryMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByQuery(String text) {
        return categoryRepository.getCategoriesByQuery(text)
                .stream()
                .map(categoryMapper::map)
                .toList();
    }

    private void updateParentCategory(Category category, UUID parentId)  {
        if (parentId != null) {
            Category parentCategory = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "Parent category not found"));
            parentCategory.addChildCategory(category);
        }
    }
    private void isCategoryNameOriginal(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new CustomException(ExceptionType.ALREADY_EXISTS, "Category with this name already exists");
        }
    }

    private void isCategoryExist(UUID categoryId) {
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new CustomException(ExceptionType.NOT_FOUND, "Category not found");
        }
    }
}
