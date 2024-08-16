package com.example.backend.service.vote.impl;

import com.example.backend.dto.vote.*;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.vote.*;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.vote.*;
import com.example.backend.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteStatusRepository voteStatusRepository;
    private final AnnouncementRepository announcementRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Vote createVote(Authentication authentication, VoteRequestDTO voteRequestDTO, Announcement announcement) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        if (member == null) {
            throw new IllegalArgumentException("Member not found with studentId: " + studentId);
        }
        if (member.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("Only administrators can create votes.");
        }

        // Vote 엔티티 생성
        Vote vote = voteRequestDTO.toEntity(announcement);

        // VoteItem이 있을 경우에만 설정
        if (voteRequestDTO.getVoteItemIds() != null && !voteRequestDTO.getVoteItemIds().isEmpty()) {
            List<VoteItem> voteItems = voteRequestDTO.getVoteItemIds().stream()
                    .map(voteItemId -> voteItemRepository.findById(voteItemId)
                            .orElseThrow(() -> new IllegalArgumentException("VoteItem not found with ID: " + voteItemId)))
                    .collect(Collectors.toList());

            vote.setVoteItems(voteItems);
        }

        return voteRepository.save(vote);
    }

    @Override
    @Transactional
    public void vote(Authentication authentication, Long voteId, Long voteItemId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다: " + voteId));

        // LocalDateTime으로 현재 시간과 비교
        if (vote.getEndDateTime() != null && vote.getEndDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표가 종료되어 투표할 수 없습니다.");
        }

        boolean hasVoted = voteStatusRepository.existsByVoteAndMember(vote, member);

        if (!vote.isRepetition() && hasVoted) {
            throw new IllegalArgumentException("해당 회원은 이미 이 투표에 참여하였습니다.");
        }

        VoteItem voteItem = voteItemRepository.findById(voteItemId)
                .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId));

        VoteStatus voteStatus = new VoteStatus();
        voteStatus.setVote(vote);
        voteStatus.setMember(member);
        voteStatus.setVoteItem(voteItem);
        voteStatus.setStatus(true);

        voteStatusRepository.save(voteStatus);
    }

    @Override
    @Transactional
    public void vote(Authentication authentication, Long voteId, List<Long> voteItemIds) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다: " + voteId));

        if (vote.getEndDateTime() != null && vote.getEndDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표가 종료되어 투표할 수 없습니다.");
        }

        boolean hasVoted = voteStatusRepository.existsByVoteAndMember(vote, member);

        if (!vote.isRepetition() && hasVoted) {
            throw new IllegalArgumentException("해당 회원은 이미 이 투표에 참여하였습니다.");
        }

        if (vote.isRepetition()) {
            List<VoteStatus> newVoteStatuses = voteItemIds.stream()
                    .map(voteItemId -> {
                        VoteItem voteItem = voteItemRepository.findById(voteItemId)
                                .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId));

                        VoteStatus voteStatus = new VoteStatus();
                        voteStatus.setVote(vote);
                        voteStatus.setMember(member);
                        voteStatus.setVoteItem(voteItem);
                        voteStatus.setStatus(true);

                        return voteStatus;
                    })
                    .collect(Collectors.toList());

            voteStatusRepository.saveAll(newVoteStatuses);
        } else {
            if (voteItemIds.size() != 1) {
                throw new IllegalArgumentException("이 투표에서는 하나의 항목에만 투표할 수 있습니다.");
            }

            Long voteItemId = voteItemIds.get(0);
            VoteItem voteItem = voteItemRepository.findById(voteItemId)
                    .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId));

            VoteStatus voteStatus = new VoteStatus();
            voteStatus.setVote(vote);
            voteStatus.setMember(member);
            voteStatus.setVoteItem(voteItem);
            voteStatus.setStatus(true);

            voteStatusRepository.save(voteStatus);
        }
    }

    @Override
    @Transactional
    public VoteResponseDTO updateVote(Authentication authentication, Long voteId, VoteRequestDTO voteRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 투표를 업데이트 할 수 있습니다.");
        }

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        if (vote.getVoteStatuses().stream().anyMatch(VoteStatus::getStatus)) {
            throw new IllegalArgumentException("이미 투표가 진행된 항목은 수정할 수 없습니다.");
        }

        Announcement announcement = announcementRepository.findById(voteRequestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다."));

        List<VoteItem> voteItems = voteRequestDTO.getVoteItemIds().stream()
                .map(voteItemId -> voteItemRepository.findById(voteItemId)
                        .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId)))
                .collect(Collectors.toList());

        vote.setSubjectTitle(voteRequestDTO.getSubjectTitle());
        vote.setRepetition(voteRequestDTO.isRepetition());
        vote.setAdditional(voteRequestDTO.isAdditional());
        vote.setAnnouncement(announcement);
        vote.setEndDateTime(voteRequestDTO.getEndDateTime());
        vote.setVoteItemIds(voteItems.stream()
                .map(VoteItem::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(",")));

        Vote updatedVote = voteRepository.save(vote);
        return VoteResponseDTO.toResponseDTO(updatedVote);
    }

    @Override
    @Transactional
    public void deleteVote(Authentication authentication, Long voteId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 삭제할 수 있습니다.");
        }

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        List<VoteItem> voteItems = voteItemRepository.findByVote(vote);
        voteItemRepository.deleteAll(voteItems);

        List<VoteStatus> voteStatuses = voteStatusRepository.findByVote(vote);
        voteStatusRepository.deleteAll(voteStatuses);

        voteRepository.delete(vote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoteResponseDTO> getAllVotes(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN && manager.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalArgumentException("관리자와 사용자만 공지사항을 생성할 수 있습니다.");
        }
        return voteRepository.findAll().stream()
                .map(VoteResponseDTO::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VoteResponseDTO getVoteById(Authentication authentication, Long voteId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN && manager.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalArgumentException("관리자와 사용자만 공지사항을 생성할 수 있습니다.");
        }
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));
        return VoteResponseDTO.toResponseDTO(vote);
    }

    @Override
    @Transactional
    public VoteItemResponseDTO addVoteItem(Authentication authentication, Long voteId, VoteItemRequestDTO voteItemRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalArgumentException("관리자와 사용자만 공지사항을 생성할 수 있습니다.");
        }

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));
        if (vote.getEndDateTime() != null && vote.getEndDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표가 종료되어 항목을 추가할 수 없습니다.");
        }

        if (voteItemRepository.existsByVoteAndContent(vote, voteItemRequestDTO.getContent())) {
            throw new IllegalArgumentException("이미 동일한 내용의 투표 항목이 존재합니다.");
        }

        VoteItem voteItem = voteItemRequestDTO.toEntity(vote);
        VoteItem savedVoteItem = voteItemRepository.save(voteItem);
        Long voteItemId = savedVoteItem.getId();

        String currentVoteItemIds = vote.getVoteItemIds();
        String updatedVoteItemIds;
        if (currentVoteItemIds.isEmpty()) {
            updatedVoteItemIds = String.valueOf(voteItemId);
        } else {
            updatedVoteItemIds = currentVoteItemIds + "," + voteItemId;
        }
        vote.setVoteItemIds(updatedVoteItemIds);
        voteRepository.save(vote);

        return VoteItemResponseDTO.toResponseDTO(savedVoteItem);
    }

    @Override
    @Transactional
    public void endVote(Authentication authentication, Long voteId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 업데이트 할 수 있습니다.");
        }

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        vote.setEndDateTime(LocalDateTime.now());
        voteRepository.save(vote);
    }

    @Override
    @Transactional
    public void deleteVoteItem(Authentication authentication, Long voteItemId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 삭제 할 수 있습니다.");
        }
        VoteItem voteItem = voteItemRepository.findById(voteItemId)
                .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다."));

        Vote vote = voteItem.getVote();

        List<VoteStatus> voteStatuses = voteStatusRepository.findByVoteItem(voteItem);
        if (!voteStatuses.isEmpty()) {
            voteStatusRepository.deleteAll(voteStatuses);
        }

        String currentVoteItemIds = vote.getVoteItemIds();
        List<Long> currentVoteItemIdsList = Arrays.stream(currentVoteItemIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        currentVoteItemIdsList.remove(voteItemId);
        String updatedVoteItemIds = currentVoteItemIdsList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        vote.setVoteItemIds(updatedVoteItemIds);
        voteRepository.save(vote);

        voteItemRepository.delete(voteItem);
    }

    @Override
    @Transactional
    public void recastVote(Authentication authentication, Long voteId, List<Long> voteItemIds) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        List<VoteStatus> existingVoteStatuses = voteStatusRepository.findByVoteAndMember(vote, member);

        List<VoteItem> newVoteItems = voteItemIds.stream()
                .map(voteItemId -> voteItemRepository.findById(voteItemId)
                        .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId)))
                .collect(Collectors.toList());

        List<VoteStatus> newVoteStatuses = newVoteItems.stream()
                .map(voteItem -> {
                    VoteStatus status = new VoteStatus();
                    status.setVote(vote);
                    status.setMember(member);
                    status.setVoteItem(voteItem);
                    status.setStatus(false);
                    return status;
                })
                .collect(Collectors.toList());

        voteStatusRepository.saveAll(newVoteStatuses);
    }
}
