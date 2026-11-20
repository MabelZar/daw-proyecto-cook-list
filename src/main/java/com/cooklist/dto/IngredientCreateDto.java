package com.cooklist.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record IngredientCreateDto(
    @NotEmpty String name,
    @NotNull Long unitId
) {
}