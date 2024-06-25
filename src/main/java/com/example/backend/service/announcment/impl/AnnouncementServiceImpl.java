package com.example.backend.service.announcment.impl;

import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.dto.announcement.AnnouncementResponseDTO;
import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.dto.vote.VoteRequestDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.vote.Vote;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.service.announcment.AnnouncementService;
import com.example.backend.service.announcment.FileService;
import com.example.backend.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final VoteService voteService;

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDTO> getAllAnnouncements(Authentication authentication) {
        return announcementRepository.findAll().stream()
                .map(AnnouncementResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponseDTO getAnnouncementById(Authentication authentication, Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
        return AnnouncementResponseDTO.toResponseDTO(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDTO> getAnnouncementsByCategory(Authentication authentication, AnnouncementCategory category) {
        return announcementRepository.findByAnnouncementCategory(category).stream()
                .map(AnnouncementResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = IOException.class)
    public AnnouncementResponseDTO createAnnouncement(Authentication authentication, AnnouncementRequestDTO announcementRequestDTO) throws IOException {
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 생성할 수 있습니다.");
        }

        List<String> imgPaths = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        if (announcementRequestDTO.getImg() != null && !announcementRequestDTO.getImg().isEmpty()) {
            imgPaths = fileService.saveFiles(announcementRequestDTO.getImg());
        }
        if (announcementRequestDTO.getFile() != null && !announcementRequestDTO.getFile().isEmpty()) {
            filePaths = fileService.saveFiles(announcementRequestDTO.getFile());
        }

        VoteRequestDTO voteRequestDTO = announcementRequestDTO.getVoteRequest();
        Vote vote = null;
        if (voteRequestDTO != null) {
            vote = voteService.createVote(authentication, voteRequestDTO);
        }

        Announcement announcement = announcementRequestDTO.toEntity(manager, imgPaths, filePaths, vote);
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        return AnnouncementResponseDTO.toResponseDTO(savedAnnouncement);
    }

    @Override
    @Transactional(rollbackFor = IOException.class)
    public AnnouncementResponseDTO updateAnnouncement(Authentication authentication, Long id, AnnouncementRequestDTO announcementRequestDTO) throws IOException {
        Announcement existingAnnouncement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));

        List<String> imgPaths = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        if (announcementRequestDTO.getImg() != null && !announcementRequestDTO.getImg().isEmpty()) {
            imgPaths = fileService.saveFiles(announcementRequestDTO.getImg());
        }
        if (announcementRequestDTO.getFile() != null && !announcementRequestDTO.getFile().isEmpty()) {
            filePaths = fileService.saveFiles(announcementRequestDTO.getFile());
        }

        VoteRequestDTO voteRequestDTO = announcementRequestDTO.getVoteRequest();
        Vote vote = null;
        if (voteRequestDTO != null) {
            vote = voteService.createVote((Authentication) existingAnnouncement.getManager(), voteRequestDTO);
        }

        existingAnnouncement.update(announcementRequestDTO, imgPaths, filePaths, vote);
        Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);

        return AnnouncementResponseDTO.toResponseDTO(updatedAnnouncement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Authentication authentication, Long id) {
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 삭제할 수 있습니다.");
        }

        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
        announcementRepository.delete(announcement);
    }

    @Override
    @Transactional
    public void hideAnnouncement(Authentication authentication, Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
        announcement.setVisible(false);
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void showAnnouncement(Authentication authentication, Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
        announcement.setVisible(true);
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponseDTO getAnnouncementWithComments(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));

        return AnnouncementResponseDTO.toResponseDTO(announcement);
    }
}
