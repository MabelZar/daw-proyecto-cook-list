package com.cooklist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findAllByUser_Id(Long userId);
    List<Recipe> findAllByUser_Role_NameOrUser_Id(
            String roleName,
            Long userId
    );
}