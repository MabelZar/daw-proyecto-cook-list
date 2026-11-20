package com.cooklist.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.RoleResponseDto;
import com.cooklist.mapper.RoleMapper;
import com.cooklist.model.Role;
import com.cooklist.repository.RoleRepository;

@Service
public class RoleService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(
            RoleRepository roleRepository,
            RoleMapper roleMapper) {

        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDto> findAll() {

        LOGGER.info("Buscando todos los roles");

        List<RoleResponseDto> roleDtoList = new ArrayList<>();

        List<Role> roles = roleRepository.findAll();
        if (CollectionUtils.isNotEmpty(roles)) {
            for (Role role : roles) {
                RoleResponseDto dto = roleMapper.toResponseDto(role);
                roleDtoList.add(dto);
            }
        }

        return roleDtoList;

    }
}