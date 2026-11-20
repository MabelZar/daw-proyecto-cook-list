package com.cooklist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.CategoryCreateDto;
import com.cooklist.dto.CategoryResponseDto;
import com.cooklist.service.CategoryService;
import com.cooklist.util.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<CategoryResponseDto>> findAll() {

        LOGGER.info("GET /categories/find-all");

        List<CategoryResponseDto> categoryResponseDtoList = categoryService.findAll();
        return ResponseEntity.ok(categoryResponseDtoList);

    }

    @GetMapping("/find-by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponseDto>> findByNameContaining(@RequestParam @NotEmpty String name) {

        LOGGER.info("GET /categories/find-by-name?name={}", name);

        List<CategoryResponseDto> categoryResponseDtoList = categoryService.findByNameContaining(name);
        return ResponseEntity.ok(categoryResponseDtoList);

    }

    @GetMapping("/find-by-id")
    public ResponseEntity<CategoryResponseDto> findById(@RequestParam @NotNull Long id) {

        LOGGER.info("GET /categories/find-by-id?id={}", id);

        try {

            CategoryResponseDto categoryResponseDto = categoryService.findById(id);
            return ResponseEntity.ok(categoryResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryCreateDto categoryCreateDto) {

        LOGGER.info("POST /categories/create");
        CategoryResponseDto categoryResponseDto = categoryService.create(categoryCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDto);

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity update(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody CategoryCreateDto categoryCreateDto) {

        LOGGER.info("PUT /categories/update/{}", id);

        try {

            categoryService.update(id, categoryCreateDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity delete(@NotNull @PathVariable Long id) {

        LOGGER.info("DELETE /categories/delete/{}", id);

        categoryService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}