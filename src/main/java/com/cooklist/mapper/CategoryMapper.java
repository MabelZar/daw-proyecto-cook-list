package com.cooklist.mapper;

import org.springframework.stereotype.Component;
import com.cooklist.dto.CategoryResponseDto;
import com.cooklist.model.Category;

@Component
public class CategoryMapper {

    public CategoryResponseDto toResponseDto(Category category) {
        return new CategoryResponseDto(
            category.getId(),
            category.getName()
        );
    }
}