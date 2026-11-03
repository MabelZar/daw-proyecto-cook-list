package com.cooklist.dto;

public record LoginResponseDto(
    String token,
    String tokenType,
    long expiresIn
) {}