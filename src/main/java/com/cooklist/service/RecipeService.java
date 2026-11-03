package com.cooklist.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.mapper.RecipeMapper;
import com.cooklist.model.Category;
import com.cooklist.model.Recipe;
import com.cooklist.model.User;
import com.cooklist.repository.CategoryRepository;
import com.cooklist.repository.RecipeRepository;
import com.cooklist.repository.UserRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RecipeMapper recipeMapper;

    public RecipeService(
            RecipeRepository recipeRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            RecipeMapper recipeMapper) {

        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.recipeMapper = recipeMapper;
    }

    @Transactional(readOnly = true)
public List<RecipeResponseDto> findAll(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(
                "El usuario autenticado no existe."
            ));

    List<Recipe> recipes;

    if ("ADMIN".equals(user.getRole().getName())) {
        // El administrador puede consultar todas las recetas.
        recipes = recipeRepository.findAll();
    } else {
        // El cliente puede consultar el catálogo y sus propias recetas.
        recipes = recipeRepository
                .findAllByUser_Role_NameOrUser_Id("ADMIN", user.getId());
    }

    return recipes.stream()
            .map(recipeMapper::toResponseDto)
            .toList();
}

    @Transactional
    public RecipeResponseDto create(RecipeCreateDto dto, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                    "El usuario autenticado no existe."
                ));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "La categoría indicada no existe."
                ));

        Recipe recipe = recipeMapper.toEntity(dto, user, category);

        Recipe savedRecipe = recipeRepository.save(recipe);

        return recipeMapper.toResponseDto(savedRecipe);
    }
}