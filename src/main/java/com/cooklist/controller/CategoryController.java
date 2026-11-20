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

        List<CategoryResponseDto> categoryResponseDtoList = categoryService.findAll();
        return ResponseEntity.ok(categoryResponseDtoList);

    }

    @GetMapping("/find-by-name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CategoryResponseDto>> findByNameContaining(@RequestParam @NotEmpty String name) {

        List<CategoryResponseDto> categoryResponseDtoList = categoryService.findByNameContaining(name);
        return ResponseEntity.ok(categoryResponseDtoList);

    }

    @GetMapping("/find-by-id")
    public ResponseEntity<CategoryResponseDto> findById(@RequestParam @NotEmpty Long id) {

        try {

            CategoryResponseDto dto = categoryService.findById(id);
            return ResponseEntity.ok(dto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryCreateDto dto) {

        CategoryResponseDto createdCategory = categoryService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity update(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody CategoryCreateDto categoryCreateDto) {

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

        categoryService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

}