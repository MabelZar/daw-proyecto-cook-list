package com.cooklist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.CategoryCreateDto;
import com.cooklist.dto.CategoryResponseDto;
import com.cooklist.mapper.CategoryMapper;
import com.cooklist.model.Category;
import com.cooklist.repository.CategoryRepository;
import com.cooklist.util.MessageConstant;
import com.cooklist.util.exception.ResourceNotFoundException;

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

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {

        List<CategoryResponseDto> categoryDtoList = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();
        if (CollectionUtils.isNotEmpty(categories)) {
            for (Category category : categories) {
                CategoryResponseDto dto = categoryMapper.toResponseDto(category);
                categoryDtoList.add(dto);
            }
        }

        return categoryDtoList;

    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findByNameContaining(String name) {

        List<CategoryResponseDto> categoryDtoList = new ArrayList<>();

        List<Category> categories = categoryRepository.findByNameContaining(name);
        if (CollectionUtils.isNotEmpty(categories)) {
            for (Category category : categories) {
                CategoryResponseDto dto = categoryMapper.toResponseDto(category);
                categoryDtoList.add(dto);
            }
        }

        return categoryDtoList;
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto findById(Long id) throws ResourceNotFoundException {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.CATEGORY_DOESNT_EXIST));

        CategoryResponseDto categoryResponseDto = categoryMapper.toResponseDto(category);

        return categoryResponseDto;

    }

    @Transactional
    public CategoryResponseDto create(CategoryCreateDto categoryCreateDto) {

        Category category = categoryMapper.toEntity(null, categoryCreateDto);

        categoryRepository.save(category);

        return categoryMapper.toResponseDto(category);

    }

    @Transactional
    public CategoryResponseDto update(Long id, CategoryCreateDto categoryCreateDto) throws ResourceNotFoundException {

        categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.CATEGORY_DOESNT_EXIST));

        Category category = categoryMapper.toEntity(id, categoryCreateDto);

        categoryRepository.save(category);

        return categoryMapper.toResponseDto(category);

    }

    @Transactional
    public void delete(Long id) {

        categoryRepository.deleteById(id);

    }

}