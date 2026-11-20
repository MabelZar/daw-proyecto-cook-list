package com.cooklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cooklist.model.Recipe;
import com.cooklist.model.User;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(value = """
            SELECT * FROM recipes r
            WHERE unaccent(LOWER(r.name)) LIKE unaccent(LOWER(CONCAT('%', :name, '%')))
                """, nativeQuery = true)
    List<Recipe> findByNameContaining(@Param("name") String name);

    @Query("""
            SELECT r FROM Recipe r
            WHERE r.user = :user OR r.user IS NULL
                """)
    List<Recipe> findAllByUserOrPublic(@Param("user") User user);

    @Query(value = """
            SELECT * FROM recipes r
            WHERE unaccent(LOWER(r.name)) LIKE unaccent(LOWER(CONCAT('%', :name, '%')))
            AND (r.user = :user OR r.user IS NULL)
            """, nativeQuery = true)
    List<Recipe> findByNameContainingAndUserOrPublic(@Param("name") String name, @Param("user") User user);

    @Query(value = """
            SELECT r FROM Recipe r
            WHERE r.id = :id
            AND (r.user = :user OR r.user IS NULL)
            """)
    Recipe findByIdAndUser(@Param("id") Long id, @Param("user") User user);
}