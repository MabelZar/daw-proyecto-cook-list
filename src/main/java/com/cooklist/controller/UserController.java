package com.cooklist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.UserCreateDto;
import com.cooklist.dto.UserResponseDto;
import com.cooklist.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> create(
            @Valid @RequestBody UserCreateDto dto) {

        UserResponseDto createdUser = userService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }
}