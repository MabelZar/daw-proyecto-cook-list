package com.cooklist.dto;

import java.math.BigDecimal;

public record ShoppingListItemDto(
    Long ingredientId,
    String ingredientName,
    String unitName,
    String unitSymbol,
    BigDecimal totalQuantity
) {
}