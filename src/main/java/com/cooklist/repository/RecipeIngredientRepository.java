package com.cooklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooklist.model.RecipeIngredient;
import com.cooklist.model.RecipeIngredientId;

public interface RecipeIngredientRepository
        extends JpaRepository<RecipeIngredient, RecipeIngredientId> {

    List<RecipeIngredient> findAllByRecipe_User_Role_NameOrRecipe_User_Id(
            String roleName,
            Long userId
    );

}