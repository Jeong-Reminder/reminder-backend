package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {
}
