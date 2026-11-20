package com.cooklist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.IngredientCreateDto;
import com.cooklist.dto.IngredientResponseDto;
import com.cooklist.mapper.IngredientMapper;
import com.cooklist.model.Ingredient;
import com.cooklist.model.Unit;
import com.cooklist.repository.IngredientRepository;
import com.cooklist.repository.UnitRepository;
import com.cooklist.util.MessageConstant;
import com.cooklist.util.exception.ResourceNotFoundException;

@Service
public class IngredientService {

    private final static Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    private final IngredientRepository ingredientRepository;
    private final UnitRepository unitRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(
            IngredientRepository ingredientRepository,
            UnitRepository unitRepository,
            IngredientMapper ingredientMapper) {

        this.ingredientRepository = ingredientRepository;
        this.unitRepository = unitRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Transactional(readOnly = true)
    public List<IngredientResponseDto> findAll() {

        LOGGER.info("Buscando todos los ingredientes");

        List<IngredientResponseDto> ingredientDtoList = new ArrayList<>();

        List<Ingredient> ingredients = ingredientRepository.findAll();
        if (CollectionUtils.isNotEmpty(ingredients)) {
            for (Ingredient ingredient : ingredients) {
                IngredientResponseDto dto = ingredientMapper.toResponseDto(ingredient);
                ingredientDtoList.add(dto);
            }
        }

        return ingredientDtoList;

    }

    @Transactional(readOnly = true)
    public List<IngredientResponseDto> findByNameContaining(String name) {

        LOGGER.info("Buscando ingredientes cuyo nombre contiene '{}'", name);

        List<IngredientResponseDto> ingredientDtoList = new ArrayList<>();

        List<Ingredient> ingredients = ingredientRepository.findByNameContaining(name);
        if (CollectionUtils.isNotEmpty(ingredients)) {
            for (Ingredient ingredient : ingredients) {
                IngredientResponseDto dto = ingredientMapper.toResponseDto(ingredient);
                ingredientDtoList.add(dto);
            }
        }

        return ingredientDtoList;
    }

    @Transactional(readOnly = true)
    public IngredientResponseDto findById(Long id) throws ResourceNotFoundException {

        LOGGER.info("Buscando el ingrediente con id {}", id);

        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(id);
        Ingredient ingredient = ingredientOpt
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.INGREDIENT_DOESNT_EXIST));

        IngredientResponseDto ingredientResponseDto = ingredientMapper.toResponseDto(ingredient);

        return ingredientResponseDto;

    }

    @Transactional
    public IngredientResponseDto create(IngredientCreateDto ingredientCreateDto) {

        Unit unit = unitRepository.getReferenceById(ingredientCreateDto.unitId());

        Ingredient ingredient = ingredientMapper.toEntity(null, ingredientCreateDto, unit);

        ingredientRepository.save(ingredient);

        LOGGER.info("Ingrediente creado con id {}", ingredient.getId());

        return ingredientMapper.toResponseDto(ingredient);

    }

    @Transactional
    public IngredientResponseDto update(Long id, IngredientCreateDto ingredientCreateDto) {

        Unit unit = unitRepository.getReferenceById(ingredientCreateDto.unitId());

        Ingredient ingredient = ingredientMapper.toEntity(id, ingredientCreateDto, unit);

        ingredientRepository.save(ingredient);

        LOGGER.info("Ingrediente actualizado con id {}", id);

        return ingredientMapper.toResponseDto(ingredient);

    }

    @Transactional
    public void delete(Long id) {

        LOGGER.info("Borrando el ingrediente con id {}", id);

        ingredientRepository.deleteById(id);

    }

}