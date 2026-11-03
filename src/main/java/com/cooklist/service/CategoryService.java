package com.cooklist.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.cooklist.model.Category;
import com.cooklist.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}