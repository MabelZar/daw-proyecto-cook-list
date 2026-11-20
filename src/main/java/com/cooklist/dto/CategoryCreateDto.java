package com.cooklist.dto;

import jakarta.validation.constraints.NotEmpty;

public record CategoryCreateDto(
    @NotEmpty String name
) {
}