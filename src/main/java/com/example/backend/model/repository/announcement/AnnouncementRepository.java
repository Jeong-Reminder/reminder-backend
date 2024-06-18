package com.example.backend.model.repository.announcement;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    @Query("SELECT a FROM Announcement a WHERE a.announcementTitle LIKE %:keyword%")
    List<Announcement> findByAnnouncementTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT a FROM Announcement a WHERE a.announcementCategory = :category")
    List<Announcement> findByAnnouncementCategory(@Param("category") AnnouncementCategory category);

    @Query("SELECT a FROM Announcement a WHERE a.visible = :visible")
    List<Announcement> findByVisible(@Param("visible") boolean visible);

    @Query("SELECT a FROM Announcement a WHERE a.managerId = :manager")
    List<Announcement> findByManagerId(@Param("manager") Member manager);
}
