package com.example.backend.service.announcment;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    Long saveFile(MultipartFile file, Announcement announcement) throws IOException;

    File getFile(Long id);

    byte[] getFileData(Long id) throws IOException;
}
