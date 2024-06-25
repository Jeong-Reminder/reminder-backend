package com.example.backend.service.recommend.impl;

import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.recommend.Recommend;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recommend.RecommendRepository;
import com.example.backend.service.recommend.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final RecommendRepository recommendRepository;
    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;

    @Override
    public RecommendResponseDTO recommend(Authentication authentication, RecommendRequestDTO requestDTO) {
        Announcement announcement = announcementRepository.findById(requestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("Announcement not found"));

        Member member = memberRepository.findById(requestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Recommend recommend = recommendRepository.findByAnnouncementAndMember(announcement, member);
        if (recommend == null) {
            recommend = requestDTO.toEntity(announcement, member);
        } else {
            recommend.setStatus(!recommend.isStatus()); // 상태 토글
        }

        Recommend savedRecommend = recommendRepository.save(recommend);
        return RecommendResponseDTO.toResponseDTO(savedRecommend);
    }

    @Override
    public void cancelRecommend(Authentication authentication, Long recommendId) {
        recommendRepository.deleteById(recommendId);
    }
}
