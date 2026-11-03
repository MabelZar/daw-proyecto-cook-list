package com.cooklist.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

public record RecipeIngredientCreateDto(

    @NotNull
    Long recipeId,

    @NotNull
    Long ingredientId,

    @NotNull
    @DecimalMin(value = "0.01")
    BigDecimal quantity

) {
}