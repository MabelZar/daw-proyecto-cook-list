package com.cooklist.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RecipeCreateDto(

    Long id,

    @NotNull
    Long categoryId,

    @NotBlank
    @Size(max = 150)
    String name,

    String description,

    @NotNull
    @Positive
    Integer preparationMinutes,

    @NotNull
    @NotEmpty
    List<RecipeIngredientCreateDto> ingredientCreateDtos

) {
}