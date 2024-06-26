package com.example.backend.model.repository.member;

import com.example.backend.model.entity.member.MemberExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberExperienceRepository extends JpaRepository<MemberExperience, Long> {
}
