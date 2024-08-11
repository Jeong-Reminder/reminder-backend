package com.example.backend.service.announcment;

import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.dto.announcement.AnnouncementResponseDTO;
import com.example.backend.dto.announcement.AnnouncementCategory;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponseDTO> getAllAnnouncements(Authentication authentication);
    AnnouncementResponseDTO getAnnouncementById(Authentication authentication, Long id);
    List<AnnouncementResponseDTO> getAnnouncementsByCategory(Authentication authentication, AnnouncementCategory category);
    AnnouncementResponseDTO createAnnouncement(Authentication authentication, AnnouncementRequestDTO announcementRequestDTO) throws IOException;
    AnnouncementResponseDTO updateAnnouncement(Authentication authentication, Long id, AnnouncementRequestDTO announcementRequestDTO) throws IOException;
    void deleteAnnouncement(Authentication authentication, Long id);
    void hideAnnouncement(Authentication authentication, Long id);
    void showAnnouncement(Authentication authentication, Long id);
    AnnouncementResponseDTO getAnnouncementWithComments(Long announcementId);
    List<AnnouncementResponseDTO> getHiddenAnnouncements(Authentication authentication);
    List<String> getContestCategoryName();
}
