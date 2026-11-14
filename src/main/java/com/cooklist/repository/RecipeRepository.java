package com.cooklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cooklist.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByUser_Id(Long userId);

    List<Recipe> findAllByUser_Role_NameOrUser_Id(
            String roleName,
            Long userId);

    @Query(value = """
            SELECT *
            FROM recipes r
            WHERE unaccent(LOWER(r.name))
                  LIKE unaccent(LOWER(CONCAT('%', :name, '%')))
            """, nativeQuery = true)
    List<Recipe> findByNameContaining(@Param("name") String name);
}