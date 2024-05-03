package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.loginstudy.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);


}