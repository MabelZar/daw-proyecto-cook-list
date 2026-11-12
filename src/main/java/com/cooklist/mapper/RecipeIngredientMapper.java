package com.cooklist.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.cooklist.dto.RecipeIngredientResponseDto;
import com.cooklist.model.Ingredient;
import com.cooklist.model.Recipe;
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

    public RecipeIngredient toEntity(
        Recipe recipe,
        Ingredient ingredient,
        BigDecimal quantity
    ) {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(quantity);
        return recipeIngredient;
    }
}