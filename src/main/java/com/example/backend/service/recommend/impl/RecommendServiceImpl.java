package com.example.backend.service.recommend.impl;

import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recommend.Recommend;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recommend.RecommendRepository;
import com.example.backend.service.recommend.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final RecommendRepository recommendRepository;
    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;

    @Override
    public RecommendResponseDTO recommend(Authentication authentication, Long announcementId, RecommendRequestDTO requestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        if (member == null) {
            throw new IllegalArgumentException("해당 학생 ID의 회원을 찾을 수 없습니다: " + studentId);
        }

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));

        Recommend recommend = recommendRepository.findByAnnouncementAndMember(announcement, member);
        if (recommend == null) {
            recommend = requestDTO.toEntity(announcement, member);
        } else {
            recommend.setStatus(!recommend.isStatus());
        }

        Recommend savedRecommend = recommendRepository.save(recommend);
        return RecommendResponseDTO.toResponseDTO(savedRecommend);
    }

    @Override
    public void cancelRecommend(Authentication authentication, Long recommendId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        if (member == null) {
            throw new IllegalArgumentException("해당 학생 ID의 회원을 찾을 수 없습니다: " + studentId);
        }

        recommendRepository.deleteById(recommendId);
    }

    @Override
    public List<RecommendResponseDTO> getMyRecommends(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        if (member == null) {
            throw new IllegalArgumentException("해당 학생 ID의 회원을 찾을 수 없습니다: " + studentId);
        }

        List<Recommend> recommends = recommendRepository.findAllByMember(member);
        return recommends.stream()
                .map(RecommendResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }
}
