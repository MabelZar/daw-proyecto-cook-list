package com.cooklist.mapper;

import org.springframework.stereotype.Component;
import com.cooklist.dto.IngredientResponseDto;
import com.cooklist.model.Ingredient;

@Component
public class IngredientMapper {

    public IngredientResponseDto toResponseDto(Ingredient ingredient) {
        return new IngredientResponseDto(
            ingredient.getId(),
            ingredient.getName(),
            ingredient.getUnit().getId(),
            ingredient.getUnit().getName(),
            ingredient.getUnit().getSymbol()
        );
    }
}