package com.cooklist.mapper;

import org.springframework.stereotype.Component;

import com.cooklist.dto.RoleResponseDto;
import com.cooklist.model.Role;

@Component
public class RoleMapper {

    public RoleResponseDto toResponseDto(Role role) {
        return new RoleResponseDto(
            role.getId(),
            role.getName()
        );
    }
}