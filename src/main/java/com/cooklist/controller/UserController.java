package com.cooklist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.cooklist.dto.UserAdminCreateDto;
import com.cooklist.dto.UserCreateDto;
import com.cooklist.dto.UserResponseDto;
import com.cooklist.dto.UserUpdateDto;
import com.cooklist.service.UserService;
import com.cooklist.util.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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

        LOGGER.info("GET /users/find-all");

        List<UserResponseDto> userResponseDtoList = userService.findAll();
        return ResponseEntity.ok(userResponseDtoList);

    }

    @GetMapping("/find-by-name")
    public ResponseEntity<List<UserResponseDto>> findByNameContaining(@RequestParam @NotEmpty String name) {

        LOGGER.info("GET /users/find-by-name?name={}", name);

        List<UserResponseDto> userResponseDtoList = userService.findByNameContaining(name);
        return ResponseEntity.ok(userResponseDtoList);

    }

    @GetMapping("/find-by-id")
    public ResponseEntity<UserResponseDto> findById(@RequestParam @NotNull Long id) {

        LOGGER.info("GET /users/find-by-id?id={}", id);

        try {

            UserResponseDto userResponseDto = userService.findById(id);
            return ResponseEntity.ok(userResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    // Alta de usuario por administrador: incluye el rol a asignar
    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> create(
            @Valid @RequestBody UserAdminCreateDto userAdminCreateDto) {

        try {

            LOGGER.info("POST /users/create");
            UserResponseDto userResponseDto = userService.createByAdmin(userAdminCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    // Solo se actualizan nombre, cumpleaños y familySize (UserUpdateDto)
    @PutMapping("/update/{id}")
    public ResponseEntity update(
            @NotNull @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto userUpdateDto) {

        LOGGER.info("PUT /users/update/{}", id);

        try {

            userService.update(id, userUpdateDto);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    // Borrado lógico: marca el usuario como inactivo
    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity deactivate(@NotNull @PathVariable Long id) {

        LOGGER.info("DELETE /users/deactivate/{}", id);

        try {

            userService.deactivate(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @GetMapping("/find-current")
    public ResponseEntity<UserResponseDto> findCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("GET /users/find-current ({})", userDetails.getUsername());

        try {

            UserResponseDto userResponseDto = userService.findCurrentUser(userDetails.getUsername());
            return ResponseEntity.ok(userResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }
    }

    @PostMapping("/create-customer")
    public ResponseEntity<UserResponseDto> createCustomer(
            @Valid @RequestBody UserCreateDto userCreateDto) {

        try {

            LOGGER.info("POST /users/create-customer");
            UserResponseDto userResponseDto = userService.createCustomer(userCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }

    }

    @PutMapping("/update-current")
    public ResponseEntity updateCurrent(@Valid @RequestBody UserUpdateDto userUpdateDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        LOGGER.info("PUT /users/update-current ({})", userDetails.getUsername());

        try {

            userService.updateCurrent(userUpdateDto, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        } catch (ResourceNotFoundException e) {

            LOGGER.warn(e.getMessage(), e);
            return ResponseEntity.notFound().build();

        }
    }

}