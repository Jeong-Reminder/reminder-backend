package com.example.backend.recommend.service;

import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.dto.recommend.RecommendResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recommend.Recommend;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recommend.RecommendRepository;
import com.example.backend.service.recommend.impl.RecommendServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendServiceImplTest {

    @Mock
    private RecommendRepository recommendRepository;

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RecommendServiceImpl recommendService;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRecommend_NewRecommend() {
        String studentId = "testStudentId";
        Long announcementId = 1L;
        RecommendRequestDTO requestDTO = new RecommendRequestDTO();

        Member member = new Member();
        member.setId(1L);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Announcement announcement = new Announcement();
        when(announcementRepository.findById(announcementId)).thenReturn(Optional.of(announcement));

        when(recommendRepository.findByAnnouncementAndMember(announcement, member)).thenReturn(null);

        Recommend recommend = new Recommend();
        when(recommendRepository.save(any(Recommend.class))).thenReturn(recommend);

        RecommendResponseDTO responseDTO = recommendService.recommend(authentication, announcementId, requestDTO);

        assertNotNull(responseDTO);
        verify(recommendRepository).save(any(Recommend.class));
    }

    @Test
    void testRecommend_ToggleRecommend() {
        String studentId = "testStudentId";
        Long announcementId = 1L;
        RecommendRequestDTO requestDTO = new RecommendRequestDTO();

        Member member = new Member();
        member.setId(1L);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Announcement announcement = new Announcement();
        when(announcementRepository.findById(announcementId)).thenReturn(Optional.of(announcement));

        Recommend recommend = new Recommend();
        recommend.setStatus(true);
        when(recommendRepository.findByAnnouncementAndMember(announcement, member)).thenReturn(recommend);

        when(recommendRepository.save(any(Recommend.class))).thenReturn(recommend);

        RecommendResponseDTO responseDTO = recommendService.recommend(authentication, announcementId, requestDTO);

        assertNotNull(responseDTO);
        assertFalse(responseDTO.isStatus());
        verify(recommendRepository).save(any(Recommend.class));
    }

    @Test
    void testCancelRecommend() {
        String studentId = "testStudentId";
        Long recommendId = 1L;

        Member member = new Member();
        member.setId(1L);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        recommendService.cancelRecommend(authentication, recommendId);

        verify(recommendRepository).deleteById(recommendId);
    }

    @Test
    void testGetMyRecommends() {
        String studentId = "testStudentId";

        Member member = new Member();
        member.setId(1L);
        when(authentication.getName()).thenReturn(studentId);
        when(memberRepository.findByStudentId(studentId)).thenReturn(member);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Recommend recommend = new Recommend();
        when(recommendRepository.findAllByMember(member)).thenReturn(Collections.singletonList(recommend));

        List<RecommendResponseDTO> responseDTOs = recommendService.getMyRecommends(authentication);

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }
}

