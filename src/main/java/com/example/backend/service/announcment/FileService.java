package com.example.backend.service.announcment;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Long saveFile(MultipartFile file, Announcement announcement) throws IOException;

    File getFile(Long id);

    byte[] getFileData(Long id) throws IOException;
}
