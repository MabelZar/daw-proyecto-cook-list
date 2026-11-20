package com.cooklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {

}