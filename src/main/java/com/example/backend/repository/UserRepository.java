package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.entity.Member;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {


    Member findByName(String name);
}