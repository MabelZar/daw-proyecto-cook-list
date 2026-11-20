package com.cooklist.dto;

import java.util.List;

public record ShoppingListResponseDto(
    List<RecipeByDateDto> recipeByDate,
    List<ShoppingListItemDto> shoppingList
) {
}