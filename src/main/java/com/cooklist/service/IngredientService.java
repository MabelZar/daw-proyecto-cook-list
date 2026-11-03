package com.cooklist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooklist.dto.IngredientResponseDto;
import com.cooklist.mapper.IngredientMapper;
import com.cooklist.repository.IngredientRepository;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(
            IngredientRepository ingredientRepository,
            IngredientMapper ingredientMapper) {

        this.ingredientRepository = ingredientRepository;
        this.ingredientMapper = ingredientMapper;
    }

    public List<IngredientResponseDto> findAll() {
        return ingredientRepository.findAll()
                .stream()
                .map(ingredientMapper::toResponseDto)
                .toList();
    }
}