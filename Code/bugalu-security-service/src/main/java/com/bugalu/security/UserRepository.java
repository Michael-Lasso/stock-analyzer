package com.bugalu.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Users, Long> {
//    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.roles WHERE u.email=?")
    Optional<Users> findByEmail(String email);
}
