package com.cooklist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.UserCreateDto;
import com.cooklist.dto.UserResponseDto;
import com.cooklist.dto.UserUpdateDto;
import com.cooklist.service.UserService;
import com.cooklist.util.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

/*
 * 
 * UserController
 * En SecurityConfig se establece que todos los métodos
 * con excepción de create-customer:POST, solo podrán
 * acceder los administradores
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/create-customer")
    public ResponseEntity<UserResponseDto> createCustomer(
            @Valid @RequestBody UserCreateDto dto) {

        try {

            UserResponseDto createdUser = userService.createCustomer(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PutMapping("/update-current")
    public ResponseEntity updateCurrent(@Valid @RequestBody UserUpdateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        try {

            userService.updateCurrent(dto, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }
    }
}