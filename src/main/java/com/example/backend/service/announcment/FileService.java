package com.example.backend.service.announcment;

import io.jsonwebtoken.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    List<String> saveFiles(List<MultipartFile> files) throws IOException, java.io.IOException;
}
