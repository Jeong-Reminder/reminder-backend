package com.example.backend.service.announcment.impl;

import com.example.backend.service.announcment.FileService;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    @Override
    public List<String> saveFiles(List<MultipartFile> files) throws IOException, java.io.IOException {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = saveFile(file);
            if (url != null) {
                urls.add(url);
            }
        }
        return urls.isEmpty() ? null : urls;
    }

    private String saveFile(MultipartFile file) throws IOException, java.io.IOException {
        if (file.isEmpty()) {
            return null;
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);

        // 디렉터리가 존재하지 않으면 생성
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        file.transferTo(path.toFile());

        return baseUrl + filename;
    }
}
