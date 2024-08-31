package com.example.backend.model.repository.announcement;

import com.example.backend.model.entity.announcement.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    File findByFileHash(String fileHash);
}
