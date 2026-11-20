package com.cooklist.dto;

public record RecipeByDateDto(
    String date,
    Long recipeId,
    String recipeName
) {
}