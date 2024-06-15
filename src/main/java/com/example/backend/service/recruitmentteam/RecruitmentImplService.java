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
        Member member = memberRepository.findById(Long.valueOf(authentication.getName()))
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Announcement announcement = announcementRepository.findById(recruitmentRequestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        Recruitment saveRecruitment = recruitmentRepository.save(recruitmentRequestDTO.toEntity(member, announcement));
        return RecruitmentResponseDTO.toResponseDTO(saveRecruitment);
    }
}
