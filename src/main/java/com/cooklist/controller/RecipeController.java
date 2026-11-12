package com.cooklist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.service.RecipeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<RecipeResponseDto>> findAll(
            Authentication authentication) {

        return ResponseEntity.ok(
                recipeService.findAll(authentication.getName()));
    }

    @GetMapping("/find-by-name")
    public List<RecipeResponseDto> getMethodName(@RequestParam @NotEmpty String name) {

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findByNameContaining(name);

        return recipeResponseDtoList;
    }

    @PostMapping("/create")
    public ResponseEntity<RecipeResponseDto> create(
            @Valid @RequestBody RecipeCreateDto dto,
            Authentication authentication) {

        RecipeResponseDto createdRecipe = recipeService.save(
                null,
                dto,
                authentication.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRecipe);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RecipeCreateDto> update(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody RecipeCreateDto dto,
            Authentication authentication) {

        recipeService.save(
                id,
                dto,
                authentication.getName());

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<RecipeResponseDto> delete(
            @NotNull @PathVariable Long id,
            Authentication authentication) {

        recipeService.delete(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}