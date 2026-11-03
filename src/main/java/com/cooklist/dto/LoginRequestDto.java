package com.cooklist.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico no es válido.")
    String email,

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 100, message = "La contraseña no puede superar los 100 caracteres.")
    String password

) {}