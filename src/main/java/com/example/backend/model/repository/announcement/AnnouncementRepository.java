package com.example.backend.model.repository.announcement;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.model.entity.announcement.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByAnnouncementCategory(AnnouncementCategory category);
}
