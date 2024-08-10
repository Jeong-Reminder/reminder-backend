package com.example.backend.controller.announcement;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.service.announcment.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<File> uploadedFiles = fileService.uploadFiles(files);
        return ResponseEntity.ok(uploadedFiles);
    }
    @PostMapping("/upload/{announcementId}")
    public ResponseEntity<?> uploadFileWithAnnouncement(@RequestParam("file") MultipartFile file,
                                                        @PathVariable("announcementId") Announcement announcement) throws IOException {
        Long fileId = fileService.saveFile(file, announcement);
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        return fileService.downloadFile(id);
    }
}
