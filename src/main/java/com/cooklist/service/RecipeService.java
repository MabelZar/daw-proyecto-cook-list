package com.cooklist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeIngredientCreateDto;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.mapper.RecipeIngredientMapper;
import com.cooklist.mapper.RecipeMapper;
import com.cooklist.model.Category;
import com.cooklist.model.Ingredient;
import com.cooklist.model.Recipe;
import com.cooklist.model.RecipeIngredient;
import com.cooklist.model.User;
import com.cooklist.repository.CategoryRepository;
import com.cooklist.repository.IngredientRepository;
import com.cooklist.repository.RecipeIngredientRepository;
import com.cooklist.repository.RecipeRepository;
import com.cooklist.repository.UserRepository;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientMapper recipeIngredientMapper;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeMapper recipeMapper;

    public RecipeService(
            RecipeRepository recipeRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            IngredientRepository ingredientRepository,
            RecipeIngredientMapper recipeIngredientMapper,
            RecipeIngredientRepository recipeIngredientRepository,
            RecipeMapper recipeMapper) {

        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIngredientMapper = recipeIngredientMapper;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeMapper = recipeMapper;
    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> findAll(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario autenticado no existe."));

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

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> findByNameContaining(String name) {

        List<RecipeResponseDto> recipeDtoList = new ArrayList<>();

        List<Recipe> recipeList = recipeRepository.findByNameContaining(name);
        if (!CollectionUtils.isEmpty(recipeList)) {
            for (Recipe recipe : recipeList) {
                RecipeResponseDto dto = recipeMapper.toResponseDto(recipe);
                recipeDtoList.add(dto);
            }
        }

        return recipeDtoList;
    }

    @Transactional
    public RecipeResponseDto save(Long id, RecipeCreateDto dto, String email) {

        boolean isNew = Objects.isNull(id);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "El usuario autenticado no existe."));

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La categoría indicada no existe."));

        Recipe recipe = recipeMapper.toEntity(
                id,
                dto,
                "ADMIN".equals(user.getRole().getName()) ? null : user,
                category);

        Recipe savedRecipe = recipeRepository.save(recipe);

        if (!isNew) {
            List<RecipeIngredient> recipeIngredientList = recipeIngredientRepository.findByRecipe(savedRecipe);

            if (!CollectionUtils.isEmpty(recipeIngredientList)) {
                recipeIngredientRepository.deleteAll(recipeIngredientList);
            }
        }

        for (RecipeIngredientCreateDto ingredientCreateDto : dto.ingredientCreateDtos()) {

            Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientCreateDto.ingredientId());
            if (ingredientOpt.isPresent()) {
                RecipeIngredient recipeIngredient = recipeIngredientMapper.toEntity(savedRecipe, ingredientOpt.get(),
                        ingredientCreateDto.quantity());
                recipeIngredientRepository.save(recipeIngredient);
            }

        }

        return recipeMapper.toResponseDto(savedRecipe);
    }

    @Transactional
    public void delete(Long id) {

        recipeRepository.deleteById(id);

    }

}