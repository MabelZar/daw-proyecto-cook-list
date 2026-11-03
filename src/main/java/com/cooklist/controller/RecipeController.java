package com.cooklist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.service.RecipeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponseDto>> findAll() {
        return ResponseEntity.ok(recipeService.findAll());
    }

    @PostMapping
    public ResponseEntity<RecipeResponseDto> create(
            @Valid @RequestBody RecipeCreateDto dto) {

        RecipeResponseDto createdRecipe = recipeService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRecipe);
    }
}