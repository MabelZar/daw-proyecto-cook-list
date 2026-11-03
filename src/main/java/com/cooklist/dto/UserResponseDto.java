package com.cooklist.dto;

import java.time.LocalDate;

public record UserResponseDto(
    Long id,
    String name,
    String email,
    Integer familySize,
    LocalDate birthDate,
    Boolean active,
    Long roleId,
    String roleName
) {
}