package com.cooklist.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(

    @NotBlank
    @Size(max = 150)
    String name,

    @NotBlank
    @Email
    @Size(max = 255)
    String email,

    @NotNull
    @Positive
    Integer familySize,

    LocalDate birthDate,

    @NotNull
    Boolean active,

    @NotNull
    Long roleId

) {
}