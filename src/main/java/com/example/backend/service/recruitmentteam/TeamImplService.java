package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.TeamRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamResponseDTO;
import com.example.backend.model.entity.recruitmentteam.AcceptMember;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.entity.recruitmentteam.Team;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.RecruitmentRepository;
import com.example.backend.model.repository.recruitmentteam.TeamMemberRepository;
import com.example.backend.model.repository.recruitmentteam.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamImplService implements TeamService{

    private final MemberRepository memberRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public TeamResponseDTO createTeam(Authentication authentication, TeamRequestDTO teamRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        Recruitment recruitment = recruitmentRepository.findById(teamRequestDTO.getRecruitmentId())
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다."));

        if (!member.getId().equals(recruitment.getMember().getId())) {
            throw new IllegalStateException("팀생성 권한이 없습니다.");
        }

        List<AcceptMember> acceptMembers = recruitment.getAcceptMembers();

        Team team = teamRequestDTO.toEntity(recruitment);
        Team saveTeam = teamRepository.save(team);

        List<TeamMember> teamMembers = teamRequestDTO.toTeamMemberEntity(saveTeam, acceptMembers);
        teamMemberRepository.saveAll(teamMembers);

        List<MemberProfile> profiles = new ArrayList<>();
        for(TeamMember teamMember : teamMembers) {
            profiles.add(teamMember.getMember().getMemberProfile());
        }

        return TeamResponseDTO.toResponseDTO(profiles, saveTeam);
    }
}
