package com.example.backend.service.announcment.impl;

import com.example.backend.dto.announcement.AnnouncementCategory;
import com.example.backend.dto.announcement.AnnouncementRequestDTO;
import com.example.backend.dto.announcement.AnnouncementResponseDTO;
import com.example.backend.dto.vote.VoteRequestDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.announcement.File;
import com.example.backend.model.entity.comment.Comment;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.vote.Vote;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.service.announcment.AnnouncementService;
import com.example.backend.service.announcment.FileService;
import com.example.backend.service.notification.NotificationService;
import com.example.backend.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;
    private final VoteService voteService;
    private final NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDTO> getAllAnnouncements(Authentication authentication) {
        int currentYear = LocalDate.now().getYear();
        int twoYearsAgo = currentYear - 2;

        return announcementRepository.findAll().stream()
                .filter(announcement -> announcement.getCreatedTime().getYear() >= twoYearsAgo)
                .map(AnnouncementResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponseDTO getAnnouncementById(Authentication authentication, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
        return AnnouncementResponseDTO.toResponseDTO(announcement);
    }


    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponseDTO> getAnnouncementsByCategory(Authentication authentication, AnnouncementCategory category) {
        int currentYear = LocalDate.now().getYear();
        int twoYearsAgo = currentYear - 2;

        return announcementRepository.findByAnnouncementCategory(category).stream()
                .filter(announcement -> announcement.getCreatedTime().getYear() >= twoYearsAgo)
                .map(announcement -> {
                    List<Vote> votes = Optional.ofNullable(announcement.getVotes()).orElseGet(ArrayList::new);
                    return AnnouncementResponseDTO.toResponseDTO(announcement, votes);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AnnouncementResponseDTO createAnnouncement(Authentication authentication, AnnouncementRequestDTO announcementRequestDTO) throws IOException {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 생성할 수 있습니다.");
        }

        Announcement announcement = announcementRequestDTO.toEntity(manager, null, null);
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        List<File> files = new ArrayList<>();

        if (announcementRequestDTO.getImg() != null) {
            for (MultipartFile imgFile : announcementRequestDTO.getImg()) {
                if (!imgFile.isEmpty()) {
                    Long fileId = fileService.saveFile(imgFile, savedAnnouncement);  // Announcement와 연관된 파일 저장
                    File file = fileService.getFile(fileId);
                    files.add(file);
                }
            }
        }

        if (announcementRequestDTO.getFile() != null) {
            for (MultipartFile file : announcementRequestDTO.getFile()) {
                if (!file.isEmpty()) {
                    Long fileId = fileService.saveFile(file, savedAnnouncement);
                    File savedFile = fileService.getFile(fileId);
                    files.add(savedFile);
                }
            }
        }

        savedAnnouncement.setFiles(files);

        VoteRequestDTO voteRequestDTO = announcementRequestDTO.getVoteRequest();
        Vote vote = null;
        if (voteRequestDTO != null) {
            vote = voteService.createVote(authentication, voteRequestDTO);
            savedAnnouncement.setVotes(List.of(vote));
        }

        // 5. Announcement 객체 업데이트 및 최종 저장
        Announcement updatedAnnouncement = announcementRepository.save(savedAnnouncement);

        return AnnouncementResponseDTO.toResponseDTO(updatedAnnouncement);
    }

    @Override
    @Transactional
    public AnnouncementResponseDTO updateAnnouncement(Authentication authentication, Long id, AnnouncementRequestDTO announcementRequestDTO) throws IOException {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 업데이트 할 수 있습니다.");
        }
        Announcement existingAnnouncement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));

        List<Long> fileIds = new ArrayList<>();

        // 이미지 파일 처리
        if (announcementRequestDTO.getImg() != null) {
            for (MultipartFile imgFile : announcementRequestDTO.getImg()) {
                if (!imgFile.isEmpty()) {
                    Long fileId = fileService.saveFile(imgFile);
                    fileIds.add(fileId);
                }
            }
        }

        // 일반 파일 처리
        if (announcementRequestDTO.getFile() != null) {
            for (MultipartFile file : announcementRequestDTO.getFile()) {
                if (!file.isEmpty()) {
                    Long fileId = fileService.saveFile(file);
                    fileIds.add(fileId);
                }
            }
        }

        VoteRequestDTO voteRequestDTO = announcementRequestDTO.getVoteRequest();
        Vote vote = null;
        if (voteRequestDTO != null) {
            vote = voteService.createVote(authentication, voteRequestDTO);
        }

        // 파일 ID들을 File 객체로 변환
        List<File> files = fileIds.stream()
                .map(fileService::getFile)
                .collect(Collectors.toList());

        existingAnnouncement.update(announcementRequestDTO, files, vote);
        Announcement updatedAnnouncement = announcementRepository.save(existingAnnouncement);

        return AnnouncementResponseDTO.toResponseDTO(updatedAnnouncement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Authentication authentication, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }

        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
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
        if (id == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }

        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 숨길 수 있습니다.");
        }
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
        announcement.setVisible(false);
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional
    public void showAnnouncement(Authentication authentication, Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }

        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 보이게 할 수 있습니다.");
        }
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));
        announcement.setVisible(true);
        announcementRepository.save(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponseDTO getAnnouncementWithComments(Long announcementId) {
        if (announcementId == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }

        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항을 찾을 수 없습니다."));

        List<Comment> comments = announcement.getComments() != null ? announcement.getComments() : new ArrayList<>();
        List<Vote> votes = Optional.ofNullable(announcement.getVotes()).orElseGet(ArrayList::new);

        return AnnouncementResponseDTO.toResponseDTO(announcement, votes);
    }
}
