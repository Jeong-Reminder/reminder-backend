package com.example.backend.model.repository.announcement;

import com.example.backend.model.entity.announcement.ContestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestCategoryRepository extends JpaRepository<ContestCategory, Long> {
    void deleteByContestCategoryName(String category);
}
