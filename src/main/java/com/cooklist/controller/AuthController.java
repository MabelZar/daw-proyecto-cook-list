package com.cooklist.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooklist.dto.LoginRequestDto;
import com.cooklist.dto.LoginResponseDto;
import com.cooklist.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest) {

        LOGGER.info("POST /auth/login ({})", loginRequest.email());

        LoginResponseDto loginResponseDto = authService.login(loginRequest);

        return ResponseEntity.ok(loginResponseDto);
    }

    // El token viaja en el header "Authorization: Bearer xxx", aquí lo invalido
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {

        LOGGER.info("POST /auth/logout");

        authService.logout(authorizationHeader);

        return ResponseEntity.noContent().build();
    }
}