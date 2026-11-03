package com.cooklist.mapper;

import org.springframework.stereotype.Component;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.model.Recipe;
import com.cooklist.model.User;
import com.cooklist.model.Category;

@Component
public class RecipeMapper {

    public RecipeResponseDto toResponseDto(Recipe recipe) {
        return new RecipeResponseDto(
            recipe.getId(),
            recipe.getUser().getId(),
            recipe.getCategory().getId(),
            recipe.getCategory().getName(),
            recipe.getName(),
            recipe.getDescription(),
            recipe.getPreparationMinutes()
        );
    }

    public Recipe toEntity(RecipeCreateDto dto, User user, Category category) {
        Recipe recipe = new Recipe();

        recipe.setUser(user);
        recipe.setCategory(category);
        recipe.setName(dto.name());
        recipe.setDescription(dto.description());
        recipe.setPreparationMinutes(dto.preparationMinutes());

        return recipe;
    }
}