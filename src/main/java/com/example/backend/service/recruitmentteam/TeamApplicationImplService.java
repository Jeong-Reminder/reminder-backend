package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.TeamApplicationRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamApplicationResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.entity.recruitmentteam.TeamApplication;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.RecruitmentRepository;
import com.example.backend.model.repository.recruitmentteam.TeamApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamApplicationImplService implements TeamApplicationService {

    private final MemberRepository memberRepository;
    private final TeamApplicationRepository teamApplicationRepository;
    private final RecruitmentRepository recruitmentRepository;

    @Override
    public TeamApplicationResponseDTO createTeamApplication(Authentication authentication,
                                                            TeamApplicationRequestDTO teamApplicationRequestDTO,
                                                            Long recruitmentId) {
        Long memberId = Long.valueOf(authentication.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다."));

        TeamApplication saveTeamApplication = teamApplicationRepository.save(teamApplicationRequestDTO.toEntity(member, recruitment));
        return TeamApplicationResponseDTO.toResponseDTO(saveTeamApplication);

    }

    @Override
    public TeamApplicationResponseDTO updateTeamApplication(Authentication authentication,
                                                            TeamApplicationRequestDTO teamApplicationRequestDTO,
                                                            Long teamApplicationId) {
        Long memberId = Long.valueOf(authentication.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        TeamApplication teamApplication = teamApplicationRepository.findById(teamApplicationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지원서가 없습니다."));

        if (!teamApplication.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("해당 지원서에 대한 권한이 없습니다.");
        }

        teamApplication.setApplicationContent(teamApplicationRequestDTO.getApplicationContent());
        TeamApplication saveTeamApplication = teamApplicationRepository.save(teamApplication);

        return TeamApplicationResponseDTO.toResponseDTO(saveTeamApplication);
    }

    @Override
    public void deleteTeamApplication(Authentication authentication, Long teamApplicationId) {
        Long memberId = Long.valueOf(authentication.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        TeamApplication teamApplication = teamApplicationRepository.findById(teamApplicationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지원서가 없습니다."));

        if(!member.getUserRole().equals(UserRole.ROLE_ADMIN)) {
            if (!teamApplication.getMember().getId().equals(member.getId())) {
                throw new IllegalArgumentException("해당 지원서에 대한 권한이 없습니다.");
            }
        }

        teamApplicationRepository.delete(teamApplication);
    }
}
