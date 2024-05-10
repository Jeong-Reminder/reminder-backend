package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.backend.model.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //Member findByStudent_Id(String student_Id);
    Member findByName(String name);

    Optional<Member> findByStudentId(String studentId);
}