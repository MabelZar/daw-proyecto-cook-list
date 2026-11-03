package com.cooklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.RecipeIngredient;
import com.cooklist.model.RecipeIngredientId;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {

}
