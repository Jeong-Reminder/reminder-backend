package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.TeamApplicationRequestDTO;
import com.example.backend.dto.recruitmentteam.TeamApplicationResponseDTO;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.recruitmentteam.ApplicationStatus;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.entity.recruitmentteam.TeamApplication;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.RecruitmentRepository;
import com.example.backend.model.repository.recruitmentteam.TeamApplicationRepository;

import java.util.Optional;
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
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long memberId = member.getId();

        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다."));

        Optional<Recruitment> recruitmentFind = recruitmentRepository.findByMemberIdAndAnnouncementId(memberId, recruitment.getAnnouncement().getId());
        if (recruitmentFind.isPresent()) {
            throw new IllegalStateException("이미 팀장으로 지원한 경진대회입니다.");
        }

        TeamApplication teamApplicationRecruitment = teamApplicationRepository.findByMemberIdAndRecruitmentId(memberId, recruitmentId);
        if (teamApplicationRecruitment != null) {
            throw new IllegalStateException("이미 지원한 모집글입니다.");
        }

        TeamApplication teamApplicationAnnouncement = teamApplicationRepository.findByRecruitment_Announcement_IdAndMemberIdAndApplicationStatus(recruitment.getAnnouncement().getId(), memberId, ApplicationStatus.ACCEPTED);
        if (teamApplicationAnnouncement != null) {
            throw new IllegalStateException("이미 팀이 있는 경진대회입니다.");
        }

        TeamApplication saveTeamApplication = teamApplicationRepository.save(teamApplicationRequestDTO.toEntity(member, recruitment));
        return TeamApplicationResponseDTO.toResponseDTO(saveTeamApplication);
    }

    @Override
    public TeamApplicationResponseDTO updateTeamApplication(Authentication authentication,
                                                            TeamApplicationRequestDTO teamApplicationRequestDTO,
                                                            Long teamApplicationId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

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
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

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
