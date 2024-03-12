package com.forum.core.service;

import com.forum.api.dto.CategoryCreateDto;
import com.forum.api.dto.CategoryDto;
import com.forum.api.dto.CategoryUpdateDto;
import com.forum.core.entity.Category;
import com.forum.core.mapper.CategoryMapper;
import com.forum.core.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public UUID createCategory(CategoryCreateDto categoryCreateDto, UUID authorId) throws BadRequestException {
        isCategoryNameOriginal(categoryCreateDto.name());
        Category category = categoryMapper.map(categoryCreateDto, authorId);
        updateParentCategory(category, categoryCreateDto.parentId());
        categoryRepository.save(category);
        return category.getId();
    }

    public void updateCategory(UUID categoryId, CategoryUpdateDto categoryUpdateDto) throws BadRequestException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BadRequestException("Category not found"));
        if (!category.getName().equals(categoryUpdateDto.name())){
            isCategoryNameOriginal(categoryUpdateDto.name());
        }
        Category updatedCategory = categoryMapper.map(categoryUpdateDto, category);
        categoryRepository.save(updatedCategory);
    }

    public void deleteCategoryHierarchyById(UUID categoryId) throws BadRequestException {
        List<Category> categories = categoryRepository.getCategoriesHierarchyById(categoryId);
        if (!categories.isEmpty()){
            categoryRepository.deleteAll(categories);
        } else {
            throw new BadRequestException("Category not found");
        }
    }

    public List<CategoryDto> getCategoryHierarchy() {
        return categoryRepository.getCategoryHierarchy()
                .stream()
                .sorted(Comparator.comparing(Category::getName))
                .filter(category -> category.getParentCategory() == null)
                .map(categoryMapper::map)
                .toList();
    }

    private void updateParentCategory(Category category, UUID parentId) throws BadRequestException {
        if (parentId != null) {
            Category parentCategory = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new BadRequestException("Parent category not found"));
            parentCategory.addChildCategory(category);
        }
    }
    private void isCategoryNameOriginal(String name) throws BadRequestException {
        Optional<Category> category = categoryRepository.findByName(name);
        if (category.isPresent()) {
            throw new BadRequestException("Category with this name already exists");
        }
    }

    public List<CategoryDto> getCategoriesByQuery(String text) {
        return categoryRepository.getCategoriesByQuery(text)
                .stream()
                .map(categoryMapper::map)
                .toList();
    }
}
