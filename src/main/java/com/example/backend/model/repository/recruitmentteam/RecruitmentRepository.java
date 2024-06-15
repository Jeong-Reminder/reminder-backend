package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import com.example.backend.model.entity.recruitmentteam.Recruitment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
    Optional<Recruitment> findByMemberIdAndAnnouncementId(Long memberId, Long announcementId);

    List<Recruitment> findByAnnouncementId(Long announcementId);
}
