package com.example.backend.controller.announcement;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.dto.announcement.AnnouncementResponseDTO;
import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.model.entity.notification.NotificationMessage;
import com.example.backend.service.announcment.AnnouncementService;
import com.example.backend.service.notification.FCM.FCMService;
import com.example.backend.service.notification.NotificationService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/announcement")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final NotificationService notificationService;
    private final FCMService fcmService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AnnouncementResponseDTO>>> getAllAnnouncements(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<AnnouncementResponseDTO> announcements = announcementService.getAllAnnouncements(authentication);
        ResponseDTO<List<AnnouncementResponseDTO>> response = ResponseDTO.<List<AnnouncementResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(announcements)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{announcement_id}")
    public ResponseEntity<ResponseDTO<AnnouncementResponseDTO>> getAnnouncementById(Authentication authentication, @PathVariable("announcement_id") Long id) {
        AnnouncementResponseDTO announcement = announcementService.getAnnouncementById(authentication, id);
        ResponseDTO<AnnouncementResponseDTO> response = ResponseDTO.<AnnouncementResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .data(announcement)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ResponseDTO<List<AnnouncementResponseDTO>>> getAnnouncementsByCategory(Authentication authentication, @PathVariable("category") AnnouncementCategory category) {
        List<AnnouncementResponseDTO> announcements = announcementService.getAnnouncementsByCategory(authentication, category);
        ResponseDTO<List<AnnouncementResponseDTO>> response = ResponseDTO.<List<AnnouncementResponseDTO>>builder()
                .status(HttpStatus.OK.value())
                .data(announcements)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<AnnouncementResponseDTO>> createAnnouncement(
            Authentication authentication,
            @ModelAttribute AnnouncementRequestDTO announcementRequestDTO,
            @RequestParam(value = "img", required = false) List<MultipartFile> img,
            @RequestParam(value = "file", required = false) List<MultipartFile> file) throws IOException {

        announcementRequestDTO.setImg(img);
        announcementRequestDTO.setFile(file);

        AnnouncementResponseDTO announcement = announcementService.createAnnouncement(authentication, announcementRequestDTO);
        ResponseDTO<AnnouncementResponseDTO> response = ResponseDTO.<AnnouncementResponseDTO>builder()
                .status(HttpStatus.CREATED.value())
                .data(announcement)
                .build();
        NotificationMessage message = NotificationMessage.builder()
                .id(UUID.randomUUID().toString())
                .title(announcement.getAnnouncementTitle())
                .content(announcement.getAnnouncementContent())
                .category("공지")
                .targetId(announcement.getId())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationService.addMessageAllStudent(message);
        fcmService.sendMessageAllStudent(message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{announcement_id}")
    public ResponseEntity<ResponseDTO<AnnouncementResponseDTO>> updateAnnouncement(
            Authentication authentication,
            @PathVariable("announcement_id") Long id,
            @ModelAttribute AnnouncementRequestDTO announcementRequestDTO,
            @RequestParam(value = "img", required = false) List<MultipartFile> img,
            @RequestParam(value = "file", required = false) List<MultipartFile> file) throws IOException {

        announcementRequestDTO.setImg(img);
        announcementRequestDTO.setFile(file);

        AnnouncementResponseDTO announcement = announcementService.updateAnnouncement(authentication, id, announcementRequestDTO);
        ResponseDTO<AnnouncementResponseDTO> response = ResponseDTO.<AnnouncementResponseDTO>builder()
                .status(HttpStatus.OK.value())
                .data(announcement)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{announcement_id}")
    public ResponseEntity<ResponseDTO<Void>> deleteAnnouncement(Authentication authentication, @PathVariable("announcement_id") Long id) {
        announcementService.deleteAnnouncement(authentication, id);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PutMapping("/hide/{announcement_id}")
    public ResponseEntity<ResponseDTO<Void>> hideAnnouncement(Authentication authentication, @PathVariable("announcement_id") Long id) {
        announcementService.hideAnnouncement(authentication, id);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/show/{announcement_id}")
    public ResponseEntity<ResponseDTO<Void>> showAnnouncement(Authentication authentication, @PathVariable("announcement_id") Long id) {
        announcementService.showAnnouncement(authentication, id);
        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
