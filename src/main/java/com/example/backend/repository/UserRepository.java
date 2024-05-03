package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String Student_Id);
    Optional<User> findByLoginId(String loginId);

    User findBy(String name);

}