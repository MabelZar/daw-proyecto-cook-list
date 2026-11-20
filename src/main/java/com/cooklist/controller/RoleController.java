package com.cooklist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooklist.dto.RoleResponseDto;
import com.cooklist.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<RoleResponseDto>> findAll() {

        LOGGER.info("GET /roles/find-all");

        List<RoleResponseDto> roleResponseDtoList = roleService.findAll();
        return ResponseEntity.ok(roleResponseDtoList);

    }
}