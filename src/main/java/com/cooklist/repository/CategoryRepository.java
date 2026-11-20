package com.cooklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cooklist.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = """
            SELECT * FROM category c
            WHERE unaccent(LOWER(c.name)) LIKE unaccent(LOWER(CONCAT('%', :name, '%')))
            """, nativeQuery = true)
    List<Category> findByNameContaining(@Param("name") String name);

}