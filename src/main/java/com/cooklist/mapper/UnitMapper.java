package com.cooklist.mapper;

import org.springframework.stereotype.Component;
import com.cooklist.dto.UnitResponseDto;
import com.cooklist.model.Unit;

@Component
public class UnitMapper {

    public UnitResponseDto toResponseDto(Unit unit) {
        return new UnitResponseDto(
            unit.getId(),
            unit.getName(),
            unit.getSymbol()
        );
    }
}