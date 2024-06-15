package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.RecruitmentRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.RecruitmentRepository;
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

    @Override
    public RecruitmentResponseDTO createRecruitment(Authentication authentication,
                                                    RecruitmentRequestDTO recruitmentRequestDTO) {
        Long memberId = Long.valueOf(authentication.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

        Announcement announcement = announcementRepository.findById(recruitmentRequestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("해당 경진대회가 없습니다."));

        recruitmentRepository.findByMemberIdAndAnnouncementId(memberId, recruitmentRequestDTO.getAnnouncementId())
                .ifPresent(existingRecruitment -> {
                    throw new IllegalStateException("이미 지원한 경진대회입니다.");
                });

        Recruitment saveRecruitment = recruitmentRepository.save(recruitmentRequestDTO.toEntity(member, announcement));
        return RecruitmentResponseDTO.toResponseDTO(saveRecruitment);
    }

    @Override
    public RecruitmentResponseDTO updateRecruitment(Authentication authentication,
                                                    RecruitmentRequestDTO recruitmentRequestDTO, Long recruitmentId) {
        Long memberId = Long.valueOf(authentication.getName());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("유저정보가 없습니다."));

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
}
