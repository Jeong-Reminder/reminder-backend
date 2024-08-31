package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recruitmentteam.Team;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeam(Team team);

    List<TeamMember> findByMember(Member member);

    List<TeamMember> findByMemberId(Long id);
}
