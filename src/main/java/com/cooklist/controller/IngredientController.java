package com.cooklist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.IngredientCreateDto;
import com.cooklist.dto.IngredientResponseDto;
import com.cooklist.service.IngredientService;
import com.cooklist.util.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final static Logger LOGGER = LoggerFactory.getLogger(IngredientController.class);
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<IngredientResponseDto>> findAll() {

        LOGGER.info("GET /ingredients/find-all");

        List<IngredientResponseDto> ingredientResponseDtoList = ingredientService.findAll();
        return ResponseEntity.ok(ingredientResponseDtoList);

    }

    @GetMapping("/find-by-name")
    public ResponseEntity<List<IngredientResponseDto>> findByNameContaining(@RequestParam @NotEmpty String name) {

        LOGGER.info("GET /ingredients/find-by-name?name={}", name);

        List<IngredientResponseDto> ingredientResponseDtoList = ingredientService.findByNameContaining(name);
        return ResponseEntity.ok(ingredientResponseDtoList);

    }

    @GetMapping("/find-by-id")
    public ResponseEntity<IngredientResponseDto> findById(@RequestParam @NotNull Long id) {

        LOGGER.info("GET /ingredients/find-by-id?id={}", id);

        try {

            IngredientResponseDto ingredientResponseDto = ingredientService.findById(id);
            return ResponseEntity.ok(ingredientResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IngredientResponseDto> create(@Valid @RequestBody IngredientCreateDto ingredientCreateDto) {

        LOGGER.info("POST /ingredients/create");
        IngredientResponseDto ingredientResponseDto = ingredientService.create(ingredientCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientResponseDto);

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity update(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody IngredientCreateDto ingredientCreateDto) {

        LOGGER.info("PUT /ingredients/update/{}", id);

        ingredientService.update(id, ingredientCreateDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {

        LOGGER.info("DELETE /ingredients/delete/{}", id);

        ingredientService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}