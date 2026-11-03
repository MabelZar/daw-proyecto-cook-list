package com.cooklist.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.UserCreateDto;
import com.cooklist.dto.UserResponseDto;
import com.cooklist.mapper.UserMapper;
import com.cooklist.model.Role;
import com.cooklist.model.User;
import com.cooklist.repository.RoleRepository;
import com.cooklist.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public UserResponseDto create(UserCreateDto dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException(
                    "El correo electrónico ya está registrado.");
        }

        Role role = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el rol CLIENT en la base de datos."));

        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = userMapper.toEntity(dto, role, encodedPassword);

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDto(savedUser);
    }
}