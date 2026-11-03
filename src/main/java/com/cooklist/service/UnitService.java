package com.cooklist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooklist.dto.UnitResponseDto;
import com.cooklist.mapper.UnitMapper;
import com.cooklist.repository.UnitRepository;

@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public UnitService(
            UnitRepository unitRepository,
            UnitMapper unitMapper) {

        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    public List<UnitResponseDto> findAll() {
        return unitRepository.findAll()
                .stream()
                .map(unitMapper::toResponseDto)
                .toList();
    }
}