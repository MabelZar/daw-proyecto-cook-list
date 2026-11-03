package com.cooklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}