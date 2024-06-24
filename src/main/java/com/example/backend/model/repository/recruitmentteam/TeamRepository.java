package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
