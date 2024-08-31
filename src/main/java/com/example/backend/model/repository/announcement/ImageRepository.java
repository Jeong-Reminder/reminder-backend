package com.example.backend.model.repository.announcement;

import com.example.backend.model.entity.announcement.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByImageHash(String imageHash);
}
