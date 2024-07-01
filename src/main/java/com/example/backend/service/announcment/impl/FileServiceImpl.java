package com.example.backend.service.announcment.impl;

import com.example.backend.service.announcment.FileService;
import io.jsonwebtoken.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public List<String> saveFiles(List<MultipartFile> files) throws IOException, java.io.IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            String path = saveFile(file);
            paths.add(path);
        }
        return paths;
    }

    private String saveFile(MultipartFile file) throws IOException, java.io.IOException {
        if (file.isEmpty()) {
            throw new IOException("Empty file.");
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + filename);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());
        return path.toString();
    }
}

