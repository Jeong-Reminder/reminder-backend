package com.example.backend.controller.announcement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/files")
public class FileController {

    private static final String UPLOAD_DIR = "uploads/";
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            // Resolve file path within upload directory
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);

            // Check if the file exists and is readable
            if (!Files.exists(file) || Files.isDirectory(file) || !Files.isReadable(file)) {
                logger.error("File does not exist or is not readable: {}", file.toString());
                return ResponseEntity.notFound().build();
            }

            // Load file as a resource
            Resource resource = new UrlResource(file.toUri());

            // Determine content type dynamically
            HttpHeaders headers = new HttpHeaders();
            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            headers.setContentType(MediaType.parseMediaType(contentType));

            // Provide the file for download
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (IOException e) {
            logger.error("Error downloading file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
