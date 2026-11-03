package com.cooklist.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.cooklist.model.Ingredient;
import com.cooklist.repository.IngredientRepository;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }
}