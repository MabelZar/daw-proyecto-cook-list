package com.cooklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}