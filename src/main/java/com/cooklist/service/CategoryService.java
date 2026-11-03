package com.cooklist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooklist.dto.CategoryResponseDto;
import com.cooklist.mapper.CategoryMapper;
import com.cooklist.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper) {

        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }
}