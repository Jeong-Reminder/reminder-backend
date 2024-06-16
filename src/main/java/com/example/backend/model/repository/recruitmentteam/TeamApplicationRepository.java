package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.TeamApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long> {
}
