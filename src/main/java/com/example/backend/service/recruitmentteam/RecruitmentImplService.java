package com.example.backend.service.recruitmentteam;

import com.example.backend.dto.recruitmentteam.RecruitmentRequestDTO;
import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.RecruitmentRepository;
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
}
