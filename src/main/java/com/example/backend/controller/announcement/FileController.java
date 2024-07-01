package com.example.backend.controller.announcement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
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
            logger.info("Downloading file: {}", filename);
            Path file = Paths.get(UPLOAD_DIR).resolve(filename);
            logger.info("Resolved file path: {}", file.toString());

            if (!Files.exists(file)) {
                logger.error("File does not exist: {}", file.toString());
                throw new RuntimeException("File does not exist: " + filename);
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                logger.info("File found: {}", filename);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                logger.error("File not readable: {}", filename);
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            logger.error("Error downloading file: {}", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
