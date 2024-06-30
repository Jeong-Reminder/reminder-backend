package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recruitmentteam.Team;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByTeamMembersIn(Collection<List<TeamMember>> teamMembers);

    void deleteByTeamCategory(String category);
}
