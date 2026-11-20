package com.cooklist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.RecipeCreateDto;
import com.cooklist.dto.RecipeResponseDto;
import com.cooklist.service.RecipeService;
import com.cooklist.util.exception.ResourceNotFoundException;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RecipeResponseDto>> findAll() {

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findAll();
        return ResponseEntity.ok(recipeResponseDtoList);

    }

    @GetMapping("/find-by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RecipeResponseDto>> findByNameContaining(@RequestParam @NotEmpty String name) {

        List<RecipeResponseDto> recipeResponseDtoList = recipeService.findByNameContaining(name);
        return ResponseEntity.ok(recipeResponseDtoList);

    }

    @GetMapping("/find-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecipeResponseDto> findById(@RequestParam @NotEmpty Long id) {

        try {

            RecipeResponseDto dto = recipeService.findById(id);
            return ResponseEntity.ok(dto);

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecipeResponseDto> create(@Valid @RequestBody RecipeCreateDto dto) {

        RecipeResponseDto createdRecipe = recipeService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecipeCreateDto> update(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody RecipeCreateDto recipeCreateDto) {

        recipeService.update(id, recipeCreateDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RecipeResponseDto> delete(@NotNull @PathVariable Long id) {

        recipeService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/find-all-for-user")
    public ResponseEntity<List<RecipeResponseDto>> findAllForUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            List<RecipeResponseDto> recipeResponseDtoList = recipeService.findAllForUser(
                    userDetails.getUsername());
            return ResponseEntity.ok(recipeResponseDtoList);

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/find-by-name-for-user")
    public ResponseEntity<List<RecipeResponseDto>> findByNameContainingForUser(
            @RequestParam @NotEmpty String name,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            List<RecipeResponseDto> recipeResponseDtoList = recipeService.findByNameContainingForUser(
                    name,
                    userDetails.getUsername());
            return ResponseEntity.ok(recipeResponseDtoList);

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/find-by-id-for-user")
    public ResponseEntity<RecipeResponseDto> findByIdForUser(
            @RequestParam @NotEmpty Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            RecipeResponseDto dto = recipeService.findByIdForUser(
                    id,
                    userDetails.getUsername());
            return ResponseEntity.ok(dto);

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/create-for-user")
    public ResponseEntity<RecipeResponseDto> createForUser(
            @Valid @RequestBody RecipeCreateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            RecipeResponseDto createdRecipe = recipeService.createForUser(
                    dto,
                    userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

    @PutMapping("/update-for-user/{id}")
    public ResponseEntity<RecipeCreateDto> updateForUser(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody RecipeCreateDto recipeCreateDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            recipeService.updateForUser(id, recipeCreateDto, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

    @DeleteMapping("/delete-for-user/{id}")
    public ResponseEntity<RecipeResponseDto> deleteForUser(
            @NotNull @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            recipeService.deleteForUser(id, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            e.printStackTrace();
            return ResponseEntity.notFound().build();

        }

    }

}