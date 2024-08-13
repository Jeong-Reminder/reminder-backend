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

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        try {
            File file = fileService.getFile(id);
            byte[] fileData = fileService.getFileData(id);

            String contentType = file.getFileType();
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(file.getOriginalFilename(), "UTF-8").replace("+", "%20"));

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
