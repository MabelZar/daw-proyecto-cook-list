package com.cooklist.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.cooklist.model.Unit;
import com.cooklist.repository.UnitRepository;

@Service
public class UnitService {

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }
}