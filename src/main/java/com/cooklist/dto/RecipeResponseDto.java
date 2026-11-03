package com.cooklist.dto;

public record RecipeResponseDto(
    Long id,
    Long userId,
    Long categoryId,
    String categoryName,
    String name,
    String description,
    Integer preparationMinutes
) {
}