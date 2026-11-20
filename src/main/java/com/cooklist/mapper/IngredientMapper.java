package com.cooklist.mapper;

import org.springframework.stereotype.Component;

import com.cooklist.dto.IngredientCreateDto;
import com.cooklist.dto.IngredientResponseDto;
import com.cooklist.model.Ingredient;
import com.cooklist.model.Unit;

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

    public Ingredient toEntity(Long id, IngredientCreateDto ingredientCreateDto, Unit unit) {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(id);
        ingredient.setName(ingredientCreateDto.name());
        ingredient.setUnit(unit);
        return ingredient;
    }
}