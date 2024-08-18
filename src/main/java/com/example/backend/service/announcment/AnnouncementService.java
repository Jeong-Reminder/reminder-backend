package com.example.backend.service.announcment;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.dto.announcement.AnnouncementDetailResponseDTO;
import com.example.backend.dto.announcement.AnnouncementResponseDTO;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface AnnouncementService {
    List<AnnouncementResponseDTO> getAllAnnouncements(Authentication authentication);

    AnnouncementDetailResponseDTO getAnnouncementById(Authentication authentication, Long id) throws IOException;

    List<AnnouncementResponseDTO> getAnnouncementsByCategory(Authentication authentication, AnnouncementCategory category);

    AnnouncementDetailResponseDTO createAnnouncement(Authentication authentication, AnnouncementRequestDTO announcementRequestDTO) throws IOException;

    AnnouncementDetailResponseDTO updateAnnouncement(Authentication authentication, Long id, AnnouncementRequestDTO announcementRequestDTO) throws IOException;

    void deleteAnnouncement(Authentication authentication, Long id);

    void hideAnnouncement(Authentication authentication, Long id);

    void showAnnouncement(Authentication authentication, Long id);

    AnnouncementDetailResponseDTO getAnnouncementWithComments(Long announcementId) throws IOException;

    List<AnnouncementResponseDTO> getHiddenAnnouncements(Authentication authentication);

    List<String> getContestCategoryName();
}
