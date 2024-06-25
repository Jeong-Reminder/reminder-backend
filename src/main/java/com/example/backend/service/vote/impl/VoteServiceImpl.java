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

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
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
    public Vote createVote(Authentication authentication, VoteRequestDTO voteRequestDTO) {
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 생성할 수 있습니다.");
        }
        Announcement announcement = announcementRepository.findById(voteRequestDTO.getAnnouncementId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항을 찾을 수 없습니다."));
        List<VoteItem> voteItems = voteRequestDTO.getVoteItemIds().stream()
                .map(voteItemId -> voteItemRepository.findById(voteItemId)
                        .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다.")))
                .toList();
        Vote vote = voteRequestDTO.toEntity(announcement);
        vote.setVoteItemIds(String.valueOf(new HashSet<>(voteItems)));
        Vote savedVote = voteRepository.save(vote);

        return savedVote;
    }

    @Override
    @Transactional
    public VoteResponseDTO updateVote(Authentication authentication, Long voteId, VoteRequestDTO voteRequestDTO) {
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 생성할 수 있습니다.");
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
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 생성할 수 있습니다.");
        }
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));
        voteRepository.delete(vote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoteResponseDTO> getAllVotes(Authentication authentication) {
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
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
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
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
        Long userId = Long.valueOf(authentication.getName());
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (user.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalArgumentException("사용자만 투표 항목을 추가할 수 있습니다.");
        }

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        if (voteItemRepository.existsByVoteAndContent(vote, voteItemRequestDTO.getContent())) {
            throw new IllegalArgumentException("이미 동일한 내용의 투표 항목이 존재합니다.");
        }

        VoteItem voteItem = voteItemRequestDTO.toEntity(vote);
        VoteItem savedVoteItem = voteItemRepository.save(voteItem);

        return VoteItemResponseDTO.toResponseDTO(savedVoteItem);
    }

    @Override
    @Transactional
    public void endVote(Authentication authentication, Long voteId) {
        Long managerId = Long.valueOf(authentication.getName());
        Member manager = memberRepository.findById(managerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (manager.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 공지사항을 생성할 수 있습니다.");
        }
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));
        vote.setEndTime(LocalTime.now());
        voteRepository.save(vote);
    }

    @Override
    @Transactional
    public void recastVote(Authentication authentication, Long voteId, Long memberId, List<Long> voteItemIds) {
        // 사용자 확인
        Long userId = Long.valueOf(authentication.getName());
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다."));
        if (user.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalArgumentException("사용자만 투표 항목을 변경할 수 있습니다.");
        }

        // 회원 및 투표 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        // 이전 투표 항목 ID 리스트
        String[] previousVoteItemIdsArray = vote.getVoteItemIds().split(",");
        List<Long> previousVoteItemIds = Arrays.stream(previousVoteItemIdsArray)
                .map(Long::parseLong)
                .toList();

        // 이전 투표 항목과 현재 투표 항목 비교
        if (!new HashSet<>(previousVoteItemIds).containsAll(voteItemIds) || !new HashSet<>(voteItemIds).containsAll(previousVoteItemIds)) {
            // 투표 상태 업데이트
            vote.setVoteItemIds(voteItemIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));

            voteRepository.save(vote);
        }

        // VoteStatus 업데이트
        List<VoteStatus> previousVotes = voteStatusRepository.findByVoteAndMember(vote, member);
        if (!previousVotes.isEmpty()) {
            voteStatusRepository.deleteAll(previousVotes);
        }
        for (Long voteItemId : voteItemIds) {
            VoteItem voteItem = voteItemRepository.findById(voteItemId)
                    .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다."));
            VoteStatus voteStatus = new VoteStatus();
            voteStatus.setVote(vote);
            voteStatus.setMember(member);
            voteStatus.setStatus(true);
            voteStatusRepository.save(voteStatus);
        }
    }

}