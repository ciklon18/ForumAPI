package com.forum.api.controller;

import com.forum.api.constant.ApiPaths;
import com.forum.api.dto.CategoryCreateDto;
import com.forum.api.dto.CategoryDto;
import com.forum.api.dto.CategoryUpdateDto;
import com.forum.core.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(ApiPaths.CATEGORY)
    public UUID createCategory(
            @Valid @RequestBody CategoryCreateDto category,
            @RequestParam UUID authorId
    ) throws BadRequestException {
        return categoryService.createCategory(category, authorId);
    }

    @PatchMapping(ApiPaths.CATEGORY_BY_ID)
    public void updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryUpdateDto category
    ) throws BadRequestException {
        categoryService.updateCategory(categoryId, category);
    }

    @DeleteMapping(ApiPaths.CATEGORY_BY_ID)
    public void deleteCategory(@PathVariable UUID categoryId) throws BadRequestException {
        categoryService.deleteCategoryHierarchyById(categoryId);
    }

    @GetMapping(ApiPaths.CATEGORY_HIERARCHY)
    public List<CategoryDto> getCategoryHierarchy() {
        return categoryService.getCategoryHierarchy();
    }

    @GetMapping(ApiPaths.CATEGORY_BY_QUERY)
    public List<CategoryDto> getCategoryByQuery(@RequestParam(required = false) String text) {
        return categoryService.getCategoriesByQuery(text);
    }

}
