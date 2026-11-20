package com.cooklist.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Creación de usuarios por parte de un administrador:
 * igual que UserCreateDto pero incluye el rol a asignar.
 */
public record UserAdminCreateDto(

    @NotBlank @Size(max = 150) String name,

    @NotBlank @Email @Size(max = 255) String email,

    @NotBlank @Size(min = 8, max = 100) String password,

    @NotNull @Positive Integer familySize,

    LocalDate birthDate,

    @NotNull Long roleId

) {
}