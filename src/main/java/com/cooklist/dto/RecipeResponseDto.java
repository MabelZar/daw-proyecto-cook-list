package com.cooklist.dto;

import java.util.List;

public record RecipeResponseDto(
        Long id,
        Long userId,
        Long categoryId,
        String categoryName,
        String name,
        String description,
        Integer preparationMinutes,
        List<RecipeIngredientResponseDto> ingredients) {
}