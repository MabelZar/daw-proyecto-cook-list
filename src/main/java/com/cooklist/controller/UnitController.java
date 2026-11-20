package com.cooklist.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooklist.dto.UnitResponseDto;
import com.cooklist.service.UnitService;

@RestController
@RequestMapping("/units")
public class UnitController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UnitController.class);
    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<UnitResponseDto>> findAll() {

        LOGGER.info("GET /units/find-all");

        List<UnitResponseDto> unitResponseDtoList = unitService.findAll();
        return ResponseEntity.ok(unitResponseDtoList);

    }
}