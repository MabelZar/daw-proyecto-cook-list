package com.cooklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooklist.model.Recipe;
import com.cooklist.model.RecipeIngredient;
import com.cooklist.model.RecipeIngredientId;

public interface RecipeIngredientRepository
        extends JpaRepository<RecipeIngredient, RecipeIngredientId> {

    List<RecipeIngredient> findByRecipe(Recipe recipe);

}