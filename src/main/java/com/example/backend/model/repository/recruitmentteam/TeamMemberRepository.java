package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
}
