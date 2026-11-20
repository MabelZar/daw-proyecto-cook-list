package com.cooklist.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.UnitResponseDto;
import com.cooklist.mapper.UnitMapper;
import com.cooklist.model.Unit;
import com.cooklist.repository.UnitRepository;

@Service
public class UnitService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UnitService.class);

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    public UnitService(
            UnitRepository unitRepository,
            UnitMapper unitMapper) {

        this.unitRepository = unitRepository;
        this.unitMapper = unitMapper;
    }

    @Transactional(readOnly = true)
    public List<UnitResponseDto> findAll() {

        LOGGER.info("Buscando todas las unidades");

        List<UnitResponseDto> unitDtoList = new ArrayList<>();

        List<Unit> units = unitRepository.findAll();
        if (CollectionUtils.isNotEmpty(units)) {
            for (Unit unit : units) {
                UnitResponseDto dto = unitMapper.toResponseDto(unit);
                unitDtoList.add(dto);
            }
        }

        return unitDtoList;

    }
}