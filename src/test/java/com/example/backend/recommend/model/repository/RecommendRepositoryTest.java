package com.example.backend.recommend.model.repository;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recommend.Recommend;
import com.example.backend.model.repository.recommend.RecommendRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RecommendRepositoryTest {

    @Autowired
    private RecommendRepository recommendRepository;

    private Announcement announcement;
    private Member member;

    @BeforeEach
    public void setUp() {
        announcement = new Announcement();
        announcement.setId(1L);

        member = new Member();
        member.setId(1L);

        Recommend recommend = new Recommend();
        recommend.setAnnouncement(announcement);
        recommend.setMember(member);
        recommend.setStatus(true);

        recommendRepository.save(recommend);
    }

    @Test
    public void testFindByAnnouncementAndMember() {
        Recommend found = recommendRepository.findByAnnouncementAndMember(announcement, member);
        assertNotNull(found);
        assertEquals(announcement.getId(), found.getAnnouncement().getId());
        assertEquals(member.getId(), found.getMember().getId());
        assertTrue(found.isStatus());
    }

    @Test
    public void testFindAllByMember() {
        List<Recommend> recommends = recommendRepository.findAllByMember(member);
        assertNotNull(recommends);
        assertFalse(recommends.isEmpty());
        assertEquals(1, recommends.size());
        assertEquals(announcement.getId(), recommends.get(0).getAnnouncement().getId());
    }
}

