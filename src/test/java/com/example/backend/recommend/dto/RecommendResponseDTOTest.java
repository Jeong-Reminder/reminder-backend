package com.example.backend.recommend.dto;

import com.example.backend.dto.recommend.RecommendResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recommend.Recommend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendResponseDTOTest {

    @Test
    public void testToResponseDTO() {
        Recommend recommend = new Recommend();
        recommend.setId(1L);

        Announcement announcement = new Announcement();
        announcement.setId(2L);
        recommend.setAnnouncement(announcement);

        Member member = new Member();
        member.setId(3L);
        recommend.setMember(member);

        recommend.setStatus(true);

        RecommendResponseDTO responseDTO = RecommendResponseDTO.toResponseDTO(recommend);

        assertNotNull(responseDTO);
        assertEquals(recommend.getId(), responseDTO.getRecommendId());
        assertEquals(recommend.getAnnouncement().getId(), responseDTO.getAnnouncementId());
        assertEquals(recommend.getMember().getId(), responseDTO.getMemberId());
        assertTrue(responseDTO.isStatus());
    }
}

