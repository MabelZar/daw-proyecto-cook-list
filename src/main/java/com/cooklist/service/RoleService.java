package com.cooklist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooklist.dto.RoleResponseDto;
import com.cooklist.mapper.RoleMapper;
import com.cooklist.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(
            RoleRepository roleRepository,
            RoleMapper roleMapper) {

        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    public List<RoleResponseDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toResponseDto)
                .toList();
    }
}