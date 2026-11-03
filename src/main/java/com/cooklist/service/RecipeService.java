package com.cooklist.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.cooklist.model.Recipe;
import com.cooklist.repository.RecipeRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }
}