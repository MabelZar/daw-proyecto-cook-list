package com.cooklist.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MealPlanRequestDto(
    @NotEmpty String date,
    @NotNull Long recipeId
) {
}