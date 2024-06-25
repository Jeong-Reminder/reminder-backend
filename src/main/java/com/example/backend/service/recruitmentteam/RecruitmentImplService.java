package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.AcceptMemberRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.recruitmentteam.AcceptMember;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.entity.recruitmentteam.TeamApplication;
import com.example.backend.model.entity.recruitmentteam.TeamMemberRole;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.AcceptMemberRepository;
import com.example.backend.model.repository.recruitmentteam.RecruitmentRepository;
import com.example.backend.model.repository.recruitmentteam.TeamApplicationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentImplService implements RecruitmentService{

    private final MemberRepository memberRepository;
    private final AnnouncementRepository announcementRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final TeamApplicationRepository teamApplicationRepository;
    private final AcceptMemberRepository acceptMemberRepository;

    @Override
    public RecruitmentResponseDTO createRecruitment(Authentication authentication,
                                                    RecruitmentRequestDTO recruitmentRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long memberId = member.getId();

        Announcement announcement = announcementRepository.findById(recruitmentRequestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("해당 경진대회가 없습니다."));

        recruitmentRepository.findByMemberIdAndAnnouncementId(memberId, recruitmentRequestDTO.getAnnouncementId())
                .ifPresent(existingRecruitment -> {
                    throw new IllegalStateException("이미 지원한 경진대회입니다.");
                });

        TeamApplication teamApplication = teamApplicationRepository.findByRecruitment_Announcement_IdAndMemberId(recruitmentRequestDTO.getAnnouncementId(), memberId);

        if (teamApplication != null) {
            throw new IllegalStateException("팀원으로 신청한 경진대회입니다.");
        }

        Recruitment saveRecruitment = recruitmentRepository.save(recruitmentRequestDTO.toEntity(member, announcement));

        AcceptMemberRequestDTO acceptMemberRequestDTO = AcceptMemberRequestDTO.builder()
                .memberId(memberId)
                .recruitmentId(recruitmentRequestDTO.getAnnouncementId())
                .build();

        AcceptMember acceptMember = acceptMemberRequestDTO.toEntity(member, saveRecruitment);
        acceptMember.setMemberRole(TeamMemberRole.LEADER);

        acceptMemberRepository.save(acceptMember);

        return RecruitmentResponseDTO.toResponseDTO(saveRecruitment);
    }

    @Override
    public RecruitmentResponseDTO updateRecruitment(Authentication authentication,
                                                    RecruitmentRequestDTO recruitmentRequestDTO, Long recruitmentId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다."));

        if (!recruitment.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("해당 모집글에 대한 권한이 없습니다.");
        }

        recruitment.setRecruitmentCategory(recruitmentRequestDTO.getRecruitmentCategory());
        recruitment.setRecruitmentContent(recruitmentRequestDTO.getRecruitmentContent());
        recruitment.setRecruitmentTitle(recruitmentRequestDTO.getRecruitmentTitle());
        recruitment.setStudentCount(recruitmentRequestDTO.getStudentCount());
        recruitment.setHopeField(recruitmentRequestDTO.getHopeField());
        recruitment.setKakaoUrl(recruitmentRequestDTO.getKakaoUrl());

        Recruitment saveRecruitment = recruitmentRepository.save(recruitment);
        return RecruitmentResponseDTO.toResponseDTO(saveRecruitment);
    }

    @Override
    public RecruitmentResponseDTO getRecruitment(Long recruitmentId) {
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다."));

        return RecruitmentResponseDTO.toResponseDTO(recruitment);
    }

    @Override
    public List<RecruitmentResponseDTO> getRecruitmentByAnnouncementId(Long announcementId) {
        List<Recruitment> recruitmentList = recruitmentRepository.findByAnnouncementId(announcementId);

        return RecruitmentResponseDTO.toResponseDTOList(recruitmentList);
    }

    @Override
    public List<RecruitmentResponseDTO> getMyRecruitment(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long memberId = member.getId();

        List<Recruitment> recruitmentList = recruitmentRepository.findByMemberId(memberId);

        return RecruitmentResponseDTO.toResponseDTOList(recruitmentList);
    }

    @Override
    public List<RecruitmentResponseDTO> getActiveRecruitment() {
        List<Recruitment> recruitmentList = recruitmentRepository.findByEndTimeAfter(LocalDateTime.now());

        return RecruitmentResponseDTO.toResponseDTOList(recruitmentList);
    }

    @Override
    public List<RecruitmentResponseDTO> getAllRecruitment() {
        List<Recruitment> recruitmentList = recruitmentRepository.findAll();

        return RecruitmentResponseDTO.toResponseDTOList(recruitmentList);
    }

    @Override
    public void deleteRecruitment(Authentication authentication, Long recruitmentId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        Recruitment recruitment = recruitmentRepository.findById(recruitmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 모집글이 없습니다."));

        if(!member.getUserRole().equals(UserRole.ROLE_ADMIN)) {
            if (!recruitment.getMember().getId().equals(member.getId())) {
                throw new IllegalArgumentException("해당 모집글에 대한 권한이 없습니다.");
            }
        }

        recruitmentRepository.delete(recruitment);
    }
}
