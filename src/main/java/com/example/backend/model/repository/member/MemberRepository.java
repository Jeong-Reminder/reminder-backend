package com.example.backend.model.repository.member;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //멤버가 존재하는지 확인
    Boolean existsByStudentId(String studentId);

    //studentId을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    Member findByStudentId(String studentId);


    List<Member> findByUserRole(UserRole userRole);
}