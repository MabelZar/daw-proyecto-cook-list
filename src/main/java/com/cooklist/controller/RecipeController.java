package com.cooklist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.MealPlanRequestDto;
import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.dto.ShoppingListResponseDto;
import com.cooklist.service.RecipeService;
import com.cooklist.util.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/find-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RecipeResponseDto>> findAll() {

        LOGGER.info("GET /recipes/find-all");

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findAll();
        return ResponseEntity.ok(recipeResponseDtoList);

    }

    @GetMapping("/find-by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RecipeResponseDto>> findByNameContaining(@RequestParam @NotEmpty String name) {

        LOGGER.info("GET /recipes/find-by-name?name={}", name);

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findByNameContaining(name);
        return ResponseEntity.ok(recipeResponseDtoList);

    }

    @GetMapping("/find-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecipeResponseDto> findById(@RequestParam @NotNull Long id) {

        LOGGER.info("GET /recipes/find-by-id?id={}", id);

        try {

        RecipeResponseDto recipeResponseDto = recipeService.findById(id);
        return ResponseEntity.ok(recipeResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecipeResponseDto> create(@Valid @RequestBody RecipeCreateDto recipeCreateDto) {

        LOGGER.info("POST /recipes/create");
        RecipeResponseDto recipeResponseDto = recipeService.create(recipeCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(recipeResponseDto);

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity update(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody RecipeCreateDto recipeCreateDto) {

        LOGGER.info("PUT /recipes/update/{}", id);

        try {

            recipeService.update(id, recipeCreateDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {

        LOGGER.info("DELETE /recipes/delete/{}", id);

        recipeService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/find-all-for-user")
    public ResponseEntity<List<RecipeResponseDto>> findAllForUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("GET /recipes/find-all-for-user ({})", userDetails.getUsername());

        try {

            List<RecipeResponseDto> recipeResponseDtoList = recipeService.findAllForUser(
                    userDetails.getUsername());
            return ResponseEntity.ok(recipeResponseDtoList);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/find-by-name-for-user")
    public ResponseEntity<List<RecipeResponseDto>> findByNameContainingForUser(
            @RequestParam @NotEmpty String name,
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("GET /recipes/find-by-name-for-user?name={} ({})", name, userDetails.getUsername());

        try {

            List<RecipeResponseDto> recipeResponseDtoList = recipeService.findByNameContainingForUser(
                    name,
                    userDetails.getUsername());
            return ResponseEntity.ok(recipeResponseDtoList);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/find-by-id-for-user")
    public ResponseEntity<RecipeResponseDto> findByIdForUser(
            @RequestParam @NotNull Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("GET /recipes/find-by-id-for-user?id={} ({})", id, userDetails.getUsername());

        try {

            RecipeResponseDto recipeResponseDto = recipeService.findByIdForUser(
                    id,
                    userDetails.getUsername());
            return ResponseEntity.ok(recipeResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/create-for-user")
    public ResponseEntity<RecipeResponseDto> createForUser(
            @Valid @RequestBody RecipeCreateDto recipeCreateDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            LOGGER.info("POST /recipes/create-for-user ({})", userDetails.getUsername());
            RecipeResponseDto recipeResponseDto = recipeService.createForUser(
                    recipeCreateDto,
                    userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(recipeResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PutMapping("/update-for-user/{id}")
    public ResponseEntity updateForUser(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody RecipeCreateDto recipeCreateDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("PUT /recipes/update-for-user/{} ({})", id, userDetails.getUsername());

        try {

            recipeService.updateForUser(id, recipeCreateDto, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @DeleteMapping("/delete-for-user/{id}")
    public ResponseEntity deleteForUser(
            @NotNull @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("DELETE /recipes/delete-for-user/{} ({})", id, userDetails.getUsername());

        try {

            recipeService.deleteForUser(id, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/calculate-shopping-list")
    public ResponseEntity<ShoppingListResponseDto> calculateShoppingList(
            @Valid @RequestBody @NotEmpty List<MealPlanRequestDto> mealPlanRequestDtoList,
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("POST /recipes/calculate-shopping-list ({})", userDetails.getUsername());

        try {

            ShoppingListResponseDto shoppingListResponseDto = recipeService.calculateShoppingList(
                    mealPlanRequestDtoList,
                    userDetails.getUsername());
            return ResponseEntity.ok(shoppingListResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

}