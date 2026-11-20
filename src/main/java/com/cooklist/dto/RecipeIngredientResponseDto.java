package com.cooklist.dto;

import java.math.BigDecimal;

public record RecipeIngredientResponseDto(
    Long recipeId,
    String recipeName,
    Long ingredientId,
    String ingredientName,
    BigDecimal quantity,
    String unitSymbol
) {
}