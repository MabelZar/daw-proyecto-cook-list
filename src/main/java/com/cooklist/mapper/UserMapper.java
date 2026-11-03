package com.cooklist.mapper;

import org.springframework.stereotype.Component;
import com.cooklist.dto.UserResponseDto;
import com.cooklist.dto.UserCreateDto;
import com.cooklist.model.User;
import com.cooklist.model.Role;

@Component
public class UserMapper {

    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getFamilySize(),
            user.getBirthDate(),
            user.getActive(),
            user.getRole().getId(),
            user.getRole().getName()
        );
    }

    public User toEntity(UserCreateDto dto, Role role, String encodedPassword) {
        User user = new User();

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPasswordHash(encodedPassword);
        user.setFamilySize(dto.familySize());
        user.setBirthDate(dto.birthDate());
        user.setActive(true);
        user.setRole(role);

        return user;
    }
}