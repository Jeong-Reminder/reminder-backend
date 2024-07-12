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
import java.util.*;
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
    public Vote createVote(Authentication authentication, VoteRequestDTO voteRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        Long managerId = member.getId();
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + managerId));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 업데이트 할 수 있습니다.");
        }

        // 게시글에 이미 투표가 있는지 확인
        Announcement announcement = announcementRepository.findById(voteRequestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다."));
        Vote existingVote = voteRepository.findByAnnouncement(announcement);
        if (existingVote != null) {
            throw new IllegalArgumentException("해당 공지사항에는 이미 투표가 존재합니다.");
        }

        List<VoteItem> voteItems = (voteRequestDTO.getVoteItemIds() != null ? voteRequestDTO.getVoteItemIds() : List.of())
                .stream()
                .map(voteItemId -> voteItemRepository.findById((Long) voteItemId)
                        .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다.")))
                .toList();

        Vote vote = voteRequestDTO.toEntity(announcement);

        // voteItemIds 설정
        if (!voteItems.isEmpty()) {
            vote.setVoteItemIds(voteItems.stream().map(VoteItem::getId).map(String::valueOf).collect(Collectors.joining(",")));
        } else {
            vote.setVoteItemIds("");
        }

        Vote savedVote = voteRepository.save(vote);
        return savedVote;
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
                        .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다.")))
                .toList();

        vote.setSubjectTitle(voteRequestDTO.getSubjectTitle());
        vote.setRepetition(voteRequestDTO.isRepetition());
        vote.setAdditional(voteRequestDTO.isAdditional());
        vote.setAnnouncement(announcement);
        vote.setEndTime(voteRequestDTO.getEndTime());
        vote.setVoteItemIds(String.valueOf(new HashSet<>(voteItems)));

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
            throw new IllegalArgumentException("관리자만 삭제을 할 수 있습니다.");
        }

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        // Vote와 관련된 모든 VoteItem 항목을 삭제합니다.
        List<VoteItem> voteItems = voteItemRepository.findByVote(vote);
        voteItemRepository.deleteAll(voteItems);

        // Vote와 관련된 모든 VoteStatus 항목을 삭제합니다.
        List<VoteStatus> voteStatuses = voteStatusRepository.findByVote(vote);
        voteStatusRepository.deleteAll(voteStatuses);

        // Vote를 삭제합니다.
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
        if (vote.getEndTime() != null && vote.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표가 종료되어 항목을 추가할 수 없습니다.");
        }

        if (voteItemRepository.existsByVoteAndContent(vote, voteItemRequestDTO.getContent())) {
            throw new IllegalArgumentException("이미 동일한 내용의 투표 항목이 존재합니다.");
        }

        // VoteItem을 저장하고 저장된 VoteItem의 ID를 가져옴
        VoteItem voteItem = voteItemRequestDTO.toEntity(vote);
        VoteItem savedVoteItem = voteItemRepository.save(voteItem);
        Long voteItemId = savedVoteItem.getId();

        // Vote 엔티티의 voteItemIds 업데이트
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

        vote.setEndTime(LocalDateTime.now());
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

        // 투표 항목이 속한 투표를 조회
        Vote vote = voteItem.getVote();

        // 투표 상태 엔티티 삭제
        List<VoteStatus> voteStatuses = voteStatusRepository.findByVoteItem(voteItem);
        if (!voteStatuses.isEmpty()) {
            voteStatusRepository.deleteAll(voteStatuses);
        }

        // Vote 엔티티의 voteItemIds 업데이트
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

        // 투표 항목 삭제
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

        // 사용자가 기존에 투표한 항목을 가져옵니다.
        List<VoteStatus> existingVoteStatuses = voteStatusRepository.findByVoteAndMember(vote, member);

        // 새로운 투표 항목들로 투표 정보 업데이트
        List<VoteItem> newVoteItems = voteItemIds.stream()
                .map(voteItemId -> voteItemRepository.findById(voteItemId)
                        .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다.")))
                .toList();

        // 새로운 투표 항목으로 새로운 투표 기록 생성
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

        // 새로운 투표 기록 저장
        voteStatusRepository.saveAll(newVoteStatuses);
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
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));
        if (vote.getEndTime() != null && vote.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("투표가 종료되어 항목을 추가할 수 없습니다.");
        }
        VoteItem voteItem = voteItemRepository.findById(voteItemId)
                .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다."));

        boolean hasVoted = voteStatusRepository.existsByVoteAndMember(vote, member);
        if (hasVoted) {
            throw new IllegalArgumentException("해당 회원은 이미 이 투표에 투표하였습니다.");
        }

        VoteStatus voteStatus = new VoteStatus();
        voteStatus.setVote(vote);
        voteStatus.setMember(member);
        voteStatus.setVoteItem(voteItem);
        voteStatus.setStatus(true);

        voteStatusRepository.save(voteStatus);
    }

}
