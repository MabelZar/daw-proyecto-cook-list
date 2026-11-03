package com.cooklist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.RecipeIngredientCreateDto;
import com.cooklist.dto.RecipeIngredientResponseDto;
import com.cooklist.mapper.RecipeIngredientMapper;
import com.cooklist.model.Ingredient;
import com.cooklist.model.Recipe;
import com.cooklist.model.RecipeIngredient;
import com.cooklist.model.RecipeIngredientId;
import com.cooklist.repository.IngredientRepository;
import com.cooklist.repository.RecipeIngredientRepository;
import com.cooklist.repository.RecipeRepository;

@Service
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;

    public RecipeIngredientService(
            RecipeIngredientRepository recipeIngredientRepository,
            RecipeRepository recipeRepository,
            IngredientRepository ingredientRepository,
            RecipeIngredientMapper recipeIngredientMapper) {

        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientMapper = recipeIngredientMapper;
    }

    @Transactional(readOnly = true)
    public List<RecipeIngredientResponseDto> findAll() {
        return recipeIngredientRepository.findAll().stream()
                .map(recipeIngredientMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public RecipeIngredientResponseDto create(RecipeIngredientCreateDto dto) {

        Recipe recipe = recipeRepository.findById(dto.recipeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La receta indicada no existe."));

        Ingredient ingredient = ingredientRepository.findById(dto.ingredientId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El ingrediente indicado no existe."));

        RecipeIngredientId id = new RecipeIngredientId(
                dto.recipeId(),
                dto.ingredientId()
        );

        if (recipeIngredientRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Este ingrediente ya está asociado a la receta.");
        }

        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId(id);
        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(dto.quantity());

        RecipeIngredient saved = recipeIngredientRepository.save(recipeIngredient);

        return recipeIngredientMapper.toResponseDto(saved);
    }
}