package com.cooklist.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeIngredientResponseDto;
import com.cooklist.model.Recipe;
import com.cooklist.model.RecipeIngredient;
import com.cooklist.model.User;
import com.cooklist.model.Category;

@Component
public class RecipeMapper {

    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeMapper(RecipeIngredientMapper recipeIngredientMapper) {
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    public RecipeResponseDto toResponseDto(Recipe recipe) {

        Long userId = Objects.isNull(recipe.getUser()) ? null : recipe.getUser().getId();

        return new RecipeResponseDto(
                recipe.getId(),
                userId,
                recipe.getCategory().getId(),
                recipe.getCategory().getName(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getPreparationMinutes(),
                null);
    }

    public RecipeResponseDto toResponseDto(Recipe recipe, List<RecipeIngredient> recipeIngredients) {

        Long userId = Objects.isNull(recipe.getUser()) ? null : recipe.getUser().getId();
        List<RecipeIngredientResponseDto> ingredients = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(recipeIngredients)) {
            for (RecipeIngredient recipeIngredient : recipeIngredients) {
                RecipeIngredientResponseDto ingredientDto = recipeIngredientMapper.toResponseDto(recipeIngredient);
                ingredients.add(ingredientDto);
            }
        }

        return new RecipeResponseDto(
                recipe.getId(),
                userId,
                recipe.getCategory().getId(),
                recipe.getCategory().getName(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getPreparationMinutes(),
                ingredients);
    }

    public Recipe toEntity(Long id, RecipeCreateDto dto, User user, Category category) {

        Recipe recipe = new Recipe();

        recipe.setId(id);

        recipe.setUser(user);
        recipe.setCategory(category);
        recipe.setName(dto.name());
        recipe.setDescription(dto.description());
        recipe.setPreparationMinutes(dto.preparationMinutes());

        return recipe;
    }
}