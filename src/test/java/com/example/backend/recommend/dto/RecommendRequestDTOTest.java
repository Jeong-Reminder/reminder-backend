package com.example.backend.recommend.dto;


import com.example.backend.dto.recommend.RecommendRequestDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recommend.Recommend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendRequestDTOTest {

    @Test
    public void testToEntity() {
        Long announcementId = 1L;
        RecommendRequestDTO dto = new RecommendRequestDTO(announcementId);

        Announcement announcement = new Announcement();
        announcement.setId(announcementId);

        Member member = new Member();
        member.setId(2L);

        Recommend recommend = dto.toEntity(announcement, member);

        assertNotNull(recommend);
        assertEquals(announcement, recommend.getAnnouncement());
        assertEquals(member, recommend.getMember());
        assertTrue(recommend.isStatus());
    }
}

