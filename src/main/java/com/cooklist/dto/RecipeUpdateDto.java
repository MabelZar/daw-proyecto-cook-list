package com.cooklist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RecipeUpdateDto(

    @NotNull
    Long categoryId,

    @NotBlank
    @Size(max = 150)
    String name,

    String description,

    @NotNull
    @Positive
    Integer preparationMinutes

) {
}