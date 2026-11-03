package com.cooklist.mapper;

import org.springframework.stereotype.Component;

import com.cooklist.dto.RecipeIngredientResponseDto;
import com.cooklist.model.RecipeIngredient;

@Component
public class RecipeIngredientMapper {

    public RecipeIngredientResponseDto toResponseDto(
            RecipeIngredient recipeIngredient) {

        return new RecipeIngredientResponseDto(
            recipeIngredient.getRecipe().getId(),
            recipeIngredient.getRecipe().getName(),
            recipeIngredient.getIngredient().getId(),
            recipeIngredient.getIngredient().getName(),
            recipeIngredient.getQuantity(),
            recipeIngredient.getIngredient().getUnit().getSymbol()
        );
    }
}