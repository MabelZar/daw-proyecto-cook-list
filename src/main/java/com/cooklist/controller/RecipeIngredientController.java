package com.cooklist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.RecipeIngredientCreateDto;
import com.cooklist.dto.RecipeIngredientResponseDto;
import com.cooklist.service.RecipeIngredientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recipe-ingredients")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    public RecipeIngredientController(
            RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeIngredientResponseDto>> findAll() {
        return ResponseEntity.ok(recipeIngredientService.findAll());
    }

    @PostMapping
    public ResponseEntity<RecipeIngredientResponseDto> create(
            @Valid @RequestBody RecipeIngredientCreateDto dto) {

        RecipeIngredientResponseDto created = recipeIngredientService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}