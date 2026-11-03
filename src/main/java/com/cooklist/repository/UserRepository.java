package com.cooklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cooklist.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}