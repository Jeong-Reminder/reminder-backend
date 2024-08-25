package com.example.backend.controller.announcement;

import com.example.backend.model.entity.announcement.File;
import com.example.backend.service.announcment.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws IOException {
        File file = fileService.getFile(id);

        Path filePath = Paths.get(file.getFilePath()).normalize();
        byte[] fileData = Files.readAllBytes(filePath);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, file.getFileType());

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }
}
