package com.cooklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}