package com.cooklist.dto;

public record IngredientResponseDto(
    Long id,
    String name,
    Long unitId,
    String unitName,
    String unitSymbol
) {
}