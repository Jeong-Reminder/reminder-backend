package com.example.backend.recommend.model.entity;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recommend.Recommend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendTest {

    @Test
    public void testRecommendEntity() {
        Recommend recommend = new Recommend();

        Announcement announcement = new Announcement();
        announcement.setId(1L);

        Member member = new Member();
        member.setId(2L);

        recommend.setId(3L);
        recommend.setAnnouncement(announcement);
        recommend.setMember(member);
        recommend.setStatus(true);

        assertEquals(3L, recommend.getId());
        assertEquals(announcement, recommend.getAnnouncement());
        assertEquals(member, recommend.getMember());
        assertTrue(recommend.isStatus());
    }
}
