package com.example.backend.service.announcment;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    Long saveFile(MultipartFile file) throws IOException;
    Long saveFile(MultipartFile file, Announcement announcement) throws IOException; // 새로운 메서드 추가
    List<File> uploadFiles(List<MultipartFile> files);
    File getFile(Long id);
    ResponseEntity<Resource> downloadFile(Long id);
}
