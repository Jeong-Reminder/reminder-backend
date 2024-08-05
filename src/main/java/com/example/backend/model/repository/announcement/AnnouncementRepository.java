package com.example.backend.model.repository.announcement;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.model.entity.announcement.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    @Query("SELECT a FROM Announcement a WHERE a.announcementCategory = :category")
    List<Announcement> findByAnnouncementCategory(@Param("category") AnnouncementCategory category);

}
