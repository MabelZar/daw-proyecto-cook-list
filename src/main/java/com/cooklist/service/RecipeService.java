package com.cooklist.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.MealPlanRequestDto;
import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeIngredientCreateDto;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.dto.RecipeByDateDto;
import com.cooklist.dto.ShoppingListItemDto;
import com.cooklist.dto.ShoppingListResponseDto;
import com.cooklist.mapper.RecipeIngredientMapper;
import com.cooklist.mapper.RecipeMapper;
import com.cooklist.model.Category;
import com.cooklist.model.Ingredient;
import com.cooklist.model.Recipe;
import com.cooklist.model.RecipeIngredient;
import com.cooklist.model.Unit;
import com.cooklist.model.User;
import com.cooklist.repository.CategoryRepository;
import com.cooklist.repository.IngredientRepository;
import com.cooklist.repository.RecipeIngredientRepository;
import com.cooklist.repository.RecipeRepository;
import com.cooklist.repository.UserRepository;
import com.cooklist.util.MessageConstant;
import com.cooklist.util.exception.ResourceNotFoundException;

@Service
public class RecipeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecipeService.class);

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
    public List<RecipeResponseDto> findAll() {

        LOGGER.info("Buscando todas las recetas");

        List<RecipeResponseDto> recipeDtoList = new ArrayList<>();

        List<Recipe> recipes = recipeRepository.findAll();
        if (CollectionUtils.isNotEmpty(recipes)) {
            for (Recipe recipe : recipes) {
                RecipeResponseDto dto = recipeMapper.toResponseDto(recipe);
                recipeDtoList.add(dto);
            }
        }

        return recipeDtoList;

    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> findByNameContaining(String name) {

        LOGGER.info("Buscando recetas cuyo nombre contiene '{}'", name);

        List<RecipeResponseDto> recipeDtoList = new ArrayList<>();

        List<Recipe> recipes = recipeRepository.findByNameContaining(name);
        if (CollectionUtils.isNotEmpty(recipes)) {
            for (Recipe recipe : recipes) {
                RecipeResponseDto dto = recipeMapper.toResponseDto(recipe);
                recipeDtoList.add(dto);
            }
        }

        return recipeDtoList;
    }

    @Transactional(readOnly = true)
    public RecipeResponseDto findById(Long id) throws ResourceNotFoundException {

        LOGGER.info("Buscando la receta con id {}", id);

        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.RECIPE_DOESNT_EXIST));
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipe);

        RecipeResponseDto recipeResponseDto = recipeMapper.toResponseDto(recipe, recipeIngredients);

        return recipeResponseDto;

    }

    @Transactional
    public RecipeResponseDto create(RecipeCreateDto recipeCreateDto) {

        LOGGER.info("Creando una receta nueva");

        Category category = categoryRepository.getReferenceById(recipeCreateDto.categoryId());

        Recipe recipe = recipeMapper.toEntity(null, recipeCreateDto, null, category);

        recipeRepository.save(recipe);

        for (RecipeIngredientCreateDto ingredientCreateDto : recipeCreateDto.ingredientCreateDtos()) {

            Ingredient ingredient = ingredientRepository.getReferenceById(ingredientCreateDto.ingredientId());
            RecipeIngredient recipeIngredient = recipeIngredientMapper.toEntity(
                    recipe,
                    ingredient,
                    ingredientCreateDto.quantity());
            recipeIngredientRepository.save(recipeIngredient);

        }

        return recipeMapper.toResponseDto(recipe);

    }

    @Transactional
    public RecipeResponseDto update(Long id, RecipeCreateDto recipeCreateDto) throws ResourceNotFoundException {

        LOGGER.info("Actualizando la receta con id {}", id);

        recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.RECIPE_DOESNT_EXIST));

        Category category = categoryRepository.getReferenceById(recipeCreateDto.categoryId());

        Recipe recipe = recipeMapper.toEntity(id, recipeCreateDto, null, category);

        recipeRepository.save(recipe);

        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipe);
        recipeIngredients.clear();

        for (RecipeIngredientCreateDto ingredientCreateDto : recipeCreateDto.ingredientCreateDtos()) {

            Ingredient ingredient = ingredientRepository.getReferenceById(ingredientCreateDto.ingredientId());
            RecipeIngredient recipeIngredient = recipeIngredientMapper.toEntity(
                    recipe,
                    ingredient,
                    ingredientCreateDto.quantity());
            recipeIngredientRepository.save(recipeIngredient);

        }

        return recipeMapper.toResponseDto(recipe);

    }

    @Transactional
    public void delete(Long id) {

        LOGGER.info("Borrando la receta con id {}", id);

        recipeRepository.deleteById(id);

    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> findAllForUser(String email) throws ResourceNotFoundException {

        LOGGER.info("Buscando todas las recetas del usuario {}", email);

        List<RecipeResponseDto> recipeDtoList = new ArrayList<>();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));
        List<Recipe> recipes = recipeRepository.findAllByUserOrPublic(user);

        if (CollectionUtils.isNotEmpty(recipes)) {
            for (Recipe recipe : recipes) {
                RecipeResponseDto dto = recipeMapper.toResponseDto(recipe);
                recipeDtoList.add(dto);
            }
        }

        return recipeDtoList;

    }

    @Transactional(readOnly = true)
    public List<RecipeResponseDto> findByNameContainingForUser(String name, String email)
            throws ResourceNotFoundException {

        LOGGER.info("Buscando recetas con nombre '{}' para el usuario {}", name, email);

        List<RecipeResponseDto> recipeDtoList = new ArrayList<>();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        List<Recipe> recipes = recipeRepository.findByNameContainingAndUserOrPublic(name, user);
        if (CollectionUtils.isNotEmpty(recipes)) {
            for (Recipe recipe : recipes) {
                RecipeResponseDto dto = recipeMapper.toResponseDto(recipe);
                recipeDtoList.add(dto);
            }
        }

        return recipeDtoList;
    }

    @Transactional(readOnly = true)
    public RecipeResponseDto findByIdForUser(Long id, String email) throws ResourceNotFoundException {

        LOGGER.info("Buscando la receta {} para el usuario {}", id, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));
        Recipe recipe = recipeRepository.findByIdAndUser(id, user);
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipe);

        RecipeResponseDto recipeResponseDto = recipeMapper.toResponseDto(recipe, recipeIngredients);

        return recipeResponseDto;
    }

    @Transactional
    public RecipeResponseDto createForUser(RecipeCreateDto recipeCreateDto, String email)
            throws ResourceNotFoundException {

        LOGGER.info("Creando receta para el usuario {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));
        Category category = categoryRepository.getReferenceById(recipeCreateDto.categoryId());

        Recipe recipe = recipeMapper.toEntity(null, recipeCreateDto, user, category);

        recipeRepository.save(recipe);

        for (RecipeIngredientCreateDto ingredientCreateDto : recipeCreateDto.ingredientCreateDtos()) {

            Ingredient ingredient = ingredientRepository.getReferenceById(ingredientCreateDto.ingredientId());
            RecipeIngredient recipeIngredient = recipeIngredientMapper.toEntity(
                    recipe,
                    ingredient,
                    ingredientCreateDto.quantity());
            recipeIngredientRepository.save(recipeIngredient);

        }

        return recipeMapper.toResponseDto(recipe);

    }

    @Transactional
    public RecipeResponseDto updateForUser(Long id, RecipeCreateDto recipeCreateDto, String email)
            throws ResourceNotFoundException {

        LOGGER.info("Actualizando la receta {} del usuario {}", id, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));
        if (Objects.isNull(recipeRepository.findByIdAndUser(id, user))) {
            throw new ResourceNotFoundException(MessageConstant.DONT_HAVE_PERMISSIONS_TO_THIS_ACTION);
        }
        Category category = categoryRepository.getReferenceById(recipeCreateDto.categoryId());

        Recipe recipeUpdated = recipeMapper.toEntity(id, recipeCreateDto, user, category);

        recipeRepository.save(recipeUpdated);

        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipeUpdated);
        recipeIngredients.clear();

        for (RecipeIngredientCreateDto ingredientCreateDto : recipeCreateDto.ingredientCreateDtos()) {

            Ingredient ingredient = ingredientRepository.getReferenceById(ingredientCreateDto.ingredientId());
            RecipeIngredient recipeIngredient = recipeIngredientMapper.toEntity(
                    recipeUpdated,
                    ingredient,
                    ingredientCreateDto.quantity());
            recipeIngredientRepository.save(recipeIngredient);

        }

        return recipeMapper.toResponseDto(recipeUpdated);

    }

    @Transactional(readOnly = true)
    public ShoppingListResponseDto calculateShoppingList(List<MealPlanRequestDto> mealPlanRequestDtoList, String email)
            throws ResourceNotFoundException {

        LOGGER.info("Calculando la lista de la compra del usuario {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        Integer familySize = user.getFamilySize();
        if (Objects.isNull(familySize)) {
            familySize = 1;
        }

        Map<Long, BigDecimal> consolidatedQuantities = new HashMap<>();
        Map<Long, Ingredient> ingredientsById = new HashMap<>();
        List<RecipeByDateDto> recipeByDateList = new ArrayList<>();

        for (MealPlanRequestDto mealPlanRequestDto : mealPlanRequestDtoList) {

            Recipe recipe = recipeRepository.findById(mealPlanRequestDto.recipeId())
                    .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.RECIPE_DOESNT_EXIST));

            RecipeByDateDto recipeByDateDto = new RecipeByDateDto(
                    mealPlanRequestDto.date(),
                    recipe.getId(),
                    recipe.getName());
            recipeByDateList.add(recipeByDateDto);

            List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipe(recipe);

            for (RecipeIngredient recipeIngredient : recipeIngredients) {

                Ingredient ingredient = recipeIngredient.getIngredient();
                ingredientsById.put(ingredient.getId(), ingredient);

                BigDecimal currentQuantity = consolidatedQuantities.get(ingredient.getId());
                if (Objects.isNull(currentQuantity)) {
                    currentQuantity = BigDecimal.ZERO;
                }

                BigDecimal newQuantity = currentQuantity.add(recipeIngredient.getQuantity());
                consolidatedQuantities.put(ingredient.getId(), newQuantity);

            }

        }

        List<ShoppingListItemDto> shoppingListItemDtoList = new ArrayList<>();

        for (Long ingredientId : consolidatedQuantities.keySet()) {

            Ingredient ingredient = ingredientsById.get(ingredientId);
            Unit unit = ingredient.getUnit();

            BigDecimal totalQuantity = consolidatedQuantities.get(ingredientId)
                    .multiply(BigDecimal.valueOf(familySize));

            ShoppingListItemDto dto = new ShoppingListItemDto(
                    ingredientId,
                    ingredient.getName(),
                    unit.getName(),
                    unit.getSymbol(),
                    totalQuantity);

            shoppingListItemDtoList.add(dto);

        }

        return new ShoppingListResponseDto(recipeByDateList, shoppingListItemDtoList);

    }

    @Transactional
    public void deleteForUser(Long id, String email) throws ResourceNotFoundException {

        LOGGER.info("Borrando la receta {} del usuario {}", id, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));
        if (Objects.isNull(recipeRepository.findByIdAndUser(id, user))) {
            throw new ResourceNotFoundException(MessageConstant.DONT_HAVE_PERMISSIONS_TO_THIS_ACTION);
        }

        recipeRepository.deleteById(id);

    }

}