package com.cooklist.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.cooklist.model.Role;
import com.cooklist.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}