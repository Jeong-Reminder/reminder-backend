package com.example.backend.service.announcment.impl;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.dto.announcement.AnnouncementResponseDTO;
import com.example.backend.dto.vote.VoteRequestDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.ContestCategory;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.announcement.Image;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.vote.Vote;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.announcement.ContestCategoryRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.service.announcment.AnnouncementService;
import com.example.backend.service.announcment.FileService;
import com.example.backend.service.announcment.ImageService;
import com.example.backend.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final ImageService imageService;
    private final VoteService voteService;
    private final ContestCategoryRepository contestCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDTO> getAllAnnouncements(Authentication authentication) {
        return announcementRepository.findAll().stream()
                .filter(announcement -> isWithinTwoYears(announcement.getCreatedTime().getYear()))
                .filter(Announcement::isVisible)
                .sorted(Comparator.comparing(Announcement::getId))
                .map(AnnouncementResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponseDTO getAnnouncementById(Authentication authentication, Long id) {
        Announcement announcement = findAnnouncementById(id);
        validateVisibility(announcement);
        return AnnouncementResponseDTO.toResponseDTO(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDTO> getAnnouncementsByCategory(Authentication authentication, AnnouncementCategory category) {
        return announcementRepository.findByAnnouncementCategory(category).stream()
                .filter(announcement -> isWithinTwoYears(announcement.getCreatedTime().getYear()))
                .filter(Announcement::isVisible)
                .sorted(Comparator.comparing(Announcement::getId))
                .map(AnnouncementResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnnouncementResponseDTO createAnnouncement(Authentication authentication, AnnouncementRequestDTO announcementRequestDTO) throws IOException {
        Member manager = validateAndGetAdmin(authentication);

        Announcement announcement = announcementRequestDTO.toEntity(manager, null, null, null);
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        if (announcementRequestDTO.getAnnouncementCategory().equals(AnnouncementCategory.CONTEST)) {
            String announcementTitle = announcementRequestDTO.getAnnouncementTitle();
            Pattern pattern = Pattern.compile("\\[(.*?)\\]");
            Matcher matcher = pattern.matcher(announcementTitle);

            saveFilesAndImages(announcementRequestDTO, savedAnnouncement);
            String contestCategoryName = null;

            if (matcher.find()) {
                contestCategoryName = matcher.group(1);
            }

            if (contestCategoryName == null) {
                throw new IllegalArgumentException("공모전 카테고리의 공지사항 제목은 [공모전]으로 시작해야 합니다.");
            }

            ContestCategory contestCategory = ContestCategory.builder()
                    .contestCategoryName(contestCategoryName)
                    .build();

            contestCategoryRepository.save(contestCategory);
        }

        VoteRequestDTO voteRequestDTO = announcementRequestDTO.getVoteRequest();
        if (voteRequestDTO != null) {
            Vote vote = voteService.createVote(authentication, voteRequestDTO);
            savedAnnouncement.setVotes(List.of(vote));
        }
        Announcement savedannouncement = announcementRepository.findAnnouncementById(savedAnnouncement.getId());

        return AnnouncementResponseDTO.toResponseDTO(savedannouncement);
    }

    @Override
    @Transactional
    public AnnouncementResponseDTO updateAnnouncement(Authentication authentication, Long id, AnnouncementRequestDTO announcementRequestDTO) throws IOException {
        validateAndGetAdmin(authentication);

        Announcement existingAnnouncement = findAnnouncementById(id);

        updateFilesAndImages(announcementRequestDTO, existingAnnouncement);

        Vote vote = null;
        if (announcementRequestDTO.getVoteRequest() != null) {
            vote = voteService.createVote(authentication, announcementRequestDTO.getVoteRequest());
        }

        existingAnnouncement.update(announcementRequestDTO, existingAnnouncement.getFiles(), existingAnnouncement.getImages(), vote);
        return AnnouncementResponseDTO.toResponseDTO(announcementRepository.save(existingAnnouncement));
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Authentication authentication, Long id) {
        Announcement announcement = getAnnouncementEntityById(authentication, id);
        announcementRepository.delete(announcement);
    }

    @Override
    @Transactional
    public void hideAnnouncement(Authentication authentication, Long id) {
        Announcement announcement = getAnnouncementEntityById(authentication, id);
        announcement.setVisible(false);
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void showAnnouncement(Authentication authentication, Long id) {
        Announcement announcement = getAnnouncementEntityById(authentication, id);
        announcement.setVisible(true);
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponseDTO getAnnouncementWithComments(Long announcementId) {
        Announcement announcement = findAnnouncementById(announcementId);
        return AnnouncementResponseDTO.toResponseDTO(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDTO> getHiddenAnnouncements(Authentication authentication) {
        validateAdmin(authentication);
        return announcementRepository.findAll().stream()
                .filter(announcement -> !announcement.isVisible())
                .sorted(Comparator.comparing(Announcement::getId))
                .map(AnnouncementResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }

    private boolean isWithinTwoYears(int year) {
        int currentYear = LocalDate.now().getYear();
        return year >= currentYear - 2;
    }

    private Announcement findAnnouncementById(Long id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
    }

    private Member validateAndGetAdmin(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        if (member.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 생성할 수 있습니다.");
        }
        return member;
    }

    private void validateAdmin(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new IllegalArgumentException("관리자만 숨겨진 공지사항을 조회할 수 있습니다.");
        }
    }

    private Announcement getAnnouncementEntityById(Authentication authentication, Long id) {
        validateAndGetAdmin(authentication);
        return findAnnouncementById(id);
    }

    private void validateVisibility(Announcement announcement) {
        if (!announcement.isVisible()) {
            throw new IllegalArgumentException("해당 공지사항은 숨김 처리되어 조회할 수 없습니다.");
        }
    }

    private void saveFilesAndImages(AnnouncementRequestDTO announcementRequestDTO, Announcement announcement) throws IOException {
        // 파일 저장 처리
        List<MultipartFile> newFiles = announcementRequestDTO.getNewFiles();
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile newFile : newFiles) {
                if (!newFile.isEmpty()) {
                    Long fileId = fileService.saveFile(newFile, announcement);
                    File savedFile = fileService.getFile(fileId);
                    announcement.getFiles().add(savedFile);
                }
            }
        }

        // 이미지 저장 처리
        List<MultipartFile> newImages = announcementRequestDTO.getNewImages();
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile newImage : newImages) {
                if (!newImage.isEmpty()) {
                    Long imageId = imageService.saveImage(newImage, announcement);
                    Image savedImage = imageService.getImage(imageId);
                    announcement.getImages().add(savedImage);
                }
            }
        }
    }


    private void updateFilesAndImages(AnnouncementRequestDTO announcementRequestDTO, Announcement announcement) throws IOException {
        // 유지해야 할 파일 및 이미지 ID 리스트 가져옴
        List<Long> fileIdsToKeep = announcementRequestDTO.getFileIds() != null ? announcementRequestDTO.getFileIds() : new ArrayList<>();
        List<Long> imageIdsToKeep = announcementRequestDTO.getImageIds() != null ? announcementRequestDTO.getImageIds() : new ArrayList<>();

        // 파일 업데이트
        announcement.getFiles().removeIf(file -> !fileIdsToKeep.contains(file.getId()));
        List<MultipartFile> newFiles = announcementRequestDTO.getNewFiles();
        if (newFiles != null && !newFiles.isEmpty()) {
            for (MultipartFile newFile : newFiles) {
                if (!newFile.isEmpty()) {
                    fileService.saveFile(newFile, announcement);
                }
            }
        }

        // 이미지 업데이트
        announcement.getImages().removeIf(image -> !imageIdsToKeep.contains(image.getId()));
        List<MultipartFile> newImages = announcementRequestDTO.getNewImages();
        if (newImages != null && !newImages.isEmpty()) {
            for (MultipartFile newImage : newImages) {
                if (!newImage.isEmpty()) {
                    imageService.saveImage(newImage, announcement);
                }
            }
        }
    }

    @Override
    public List<String> getContestCategoryName() {
        return contestCategoryRepository.findAll().stream()
                .map(ContestCategory::getContestCategoryName)
                .collect(Collectors.toList());
    }
}
