package com.cooklist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.UserCreateDto;
import com.cooklist.dto.UserResponseDto;
import com.cooklist.dto.UserUpdateDto;
import com.cooklist.mapper.UserMapper;
import com.cooklist.model.Role;
import com.cooklist.model.User;
import com.cooklist.repository.RoleRepository;
import com.cooklist.repository.UserRepository;
import com.cooklist.util.DataConstant;
import com.cooklist.util.MessageConstant;
import com.cooklist.util.exception.ResourceNotFoundException;

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

        List<UserResponseDto> userResponseDtos = new ArrayList<>();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            UserResponseDto userResponseDto = userMapper.toResponseDto(user);
            userResponseDtos.add(userResponseDto);
        }

        return userResponseDtos;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {

        UserResponseDto userResponseDto = null;

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            userResponseDto = userMapper.toResponseDto(userOpt.get());
        }

        return userResponseDto;
    }

    @Transactional
    public UserResponseDto createCustomer(UserCreateDto dto) throws ResourceNotFoundException {

        if (userRepository.existsByEmail(dto.email())) {
            throw new ResourceNotFoundException(MessageConstant.EMAIL_EXISTS);
        }
        Role role = roleRepository.findByName(DataConstant.CLIENT_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.CLIENT_ROLE_DOESNT_EXIST));

        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = userMapper.toEntity(dto, role, encodedPassword);
        user.setActive(Boolean.TRUE);
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    @Transactional
    public void updateCurrent(UserUpdateDto userUpdateDto, String email) throws ResourceNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        user.setName(userUpdateDto.name());
        user.setFamilySize(userUpdateDto.familySize());
        user.setBirthDate(userUpdateDto.birthDate());

        userRepository.save(user);

    }
}