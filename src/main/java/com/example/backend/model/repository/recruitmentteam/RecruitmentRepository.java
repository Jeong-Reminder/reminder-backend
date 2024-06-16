package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.Recruitment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Optional<Recruitment> findByMemberIdAndAnnouncementId(Long memberId, Long announcementId);

    List<Recruitment> findByAnnouncementId(Long announcementId);

    List<Recruitment> findByMemberId(Long memberId);

    List<Recruitment> findByEndTimeAfter(LocalDateTime now);
}
