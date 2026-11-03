package com.cooklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}