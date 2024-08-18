package com.example.backend.service.vote.impl;

import com.example.backend.dto.vote.VoteItemRequestDTO;
import com.example.backend.dto.vote.VoteItemResponseDTO;
import com.example.backend.dto.vote.VoteRequestDTO;
import com.example.backend.dto.vote.VoteResponseDTO;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.entity.vote.Vote;
import com.example.backend.model.entity.vote.VoteItem;
import com.example.backend.model.entity.vote.VoteStatus;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.vote.VoteItemRepository;
import com.example.backend.model.repository.vote.VoteRepository;
import com.example.backend.model.repository.vote.VoteStatusRepository;
import com.example.backend.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
            throw new IllegalArgumentException("해당하는 회원을 찾지 못했습니다: " + studentId);
        }
        if (member.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 투표를 생성할 수 있습니다.");
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        if (voteRequestDTO.getEndDateTime() != null && voteRequestDTO.getEndDateTime().isBefore(now.toLocalDateTime())) {
            throw new IllegalArgumentException("종료 시간은 현재 시간 이후로 설정해야 합니다.");
        }

        Vote vote = voteRequestDTO.toEntity(announcement);

        if (voteRequestDTO.getVoteItemIds() != null && !voteRequestDTO.getVoteItemIds().isEmpty()) {
            List<VoteItem> voteItems = voteRequestDTO.getVoteItemIds().stream()
                    .map(voteItemId -> voteItemRepository.findById(voteItemId)
                            .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId)))
                    .collect(Collectors.toList());
            vote.setVoteItems(voteItems);
        }

        return voteRepository.save(vote);
    }

    @Override
    @Transactional
    public VoteResponseDTO updateVote(Authentication authentication, Long voteId, VoteRequestDTO voteRequestDTO) {
        Member manager = getAuthenticatedAdmin(authentication);

        Vote vote = checkVoteEndTime(voteId);
        if (vote.isVoteEnded()) {
            throw new IllegalArgumentException("종료된 투표는 수정할 수 없습니다.");
        }

        if (vote.getVoteStatuses().stream().anyMatch(VoteStatus::getStatus)) {
            throw new IllegalArgumentException("이미 투표가 진행된 항목은 수정할 수 없습니다.");
        }

        if (voteRequestDTO.getEndDateTime() != null && voteRequestDTO.getEndDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("종료 시간은 현재 시간 이후로 설정해야 합니다.");
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

        return VoteResponseDTO.toResponseDTO(voteRepository.save(vote), false);
    }

    @Override
    @Transactional
    public VoteItemResponseDTO addVoteItem(Authentication authentication, Long voteId, VoteItemRequestDTO voteItemRequestDTO) {
        Vote vote = checkVoteEndTime(voteId);

        if (vote.isVoteEnded()) {
            throw new IllegalArgumentException("이 투표는 이미 종료되었습니다.");
        }

        Member member = getAuthenticatedMember(authentication);

        if (member.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalArgumentException("사용자만 투표 항목을 추가할 수 있습니다.");
        }

        boolean existsDuplicateContent = voteItemRepository.existsByVoteAndContent(vote, voteItemRequestDTO.getContent());
        if (existsDuplicateContent) {
            throw new IllegalArgumentException("이미 동일한 내용의 투표 항목이 존재합니다.");
        }

        VoteItem voteItem = voteItemRequestDTO.toEntity(vote);
        VoteItem savedVoteItem = voteItemRepository.save(voteItem);

        String currentVoteItemIds = vote.getVoteItemIds();
        String updatedVoteItemIds;
        if (currentVoteItemIds == null || currentVoteItemIds.isEmpty()) {
            updatedVoteItemIds = String.valueOf(savedVoteItem.getId());
        } else {
            updatedVoteItemIds = currentVoteItemIds + "," + savedVoteItem.getId();
        }
        vote.setVoteItemIds(updatedVoteItemIds);
        voteRepository.save(vote);

        return VoteItemResponseDTO.toResponseDTO(savedVoteItem);
    }

    @Override
    @Transactional
    public void vote(Authentication authentication, Long voteId, Long voteItemId) {
        Vote vote = checkVoteEndTime(voteId);

        if (vote.isVoteEnded()) {
            throw new IllegalArgumentException("이 투표는 이미 종료되었습니다.");
        }

        Member member = getAuthenticatedMember(authentication);
        boolean hasVoted = voteStatusRepository.existsByVoteAndMember(vote, member);
        if (!vote.isRepetition() && hasVoted) {
            throw new IllegalArgumentException("해당 회원은 이미 이 투표에 참여하였습니다.");
        }

        VoteItem voteItem = voteItemRepository.findById(voteItemId)
                .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId));

        if (!vote.getVoteItems().contains(voteItem)) {
            throw new IllegalArgumentException("이 투표 항목은 해당 투표에 속하지 않습니다.");
        }

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
        Vote vote = checkVoteEndTime(voteId);

        if (vote.isVoteEnded()) {
            throw new IllegalArgumentException("이 투표는 이미 종료되었습니다.");
        }

        Member member = getAuthenticatedMember(authentication);
        boolean hasVoted = voteStatusRepository.existsByVoteAndMember(vote, member);
        if (!vote.isRepetition() && hasVoted) {
            throw new IllegalArgumentException("해당 회원은 이미 이 투표에 참여하였습니다.");
        }
        for (Long voteItemId : voteItemIds) {
            VoteItem voteItem = voteItemRepository.findById(voteItemId)
                    .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId));
            if (!vote.getVoteItems().contains(voteItem)) {
                throw new IllegalArgumentException("이 투표 항목은 해당 투표에 속하지 않습니다: " + voteItemId);
            }
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
                    }).collect(Collectors.toList());

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
    public void recastVote(Authentication authentication, Long voteId, List<Long> voteItemIds) {
        Member member = getAuthenticatedMember(authentication);

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        if (vote.isVoteEnded()) {
            throw new IllegalArgumentException("이 투표는 이미 종료되었습니다.");
        }

        List<VoteStatus> existingVoteStatuses = voteStatusRepository.findByVoteAndMember(vote, member);
        voteStatusRepository.deleteAll(existingVoteStatuses);

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
                }).collect(Collectors.toList());

        voteStatusRepository.saveAll(newVoteStatuses);
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void checkAndCloseVotes() {
        LocalDateTime now = LocalDateTime.now();

        List<Vote> openVotes = voteRepository.findByVoteEndedFalseAndEndDateTimeBefore(now);

        for (Vote vote : openVotes) {
            vote.setVoteEnded(true);
            voteRepository.save(vote);
        }
    }

    @Override
    @Transactional
    public void deleteVote(Authentication authentication, Long voteId) {
        Member manager = getAuthenticatedAdmin(authentication);

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        List<VoteItem> voteItems = voteItemRepository.findByVote(vote);
        voteItemRepository.deleteAll(voteItems);

        List<VoteStatus> voteStatuses = voteStatusRepository.findByVote(vote);
        voteStatusRepository.deleteAll(voteStatuses);

        voteRepository.delete(vote);
    }

    @Override
    @Transactional
    public void deleteVoteItem(Authentication authentication, Long voteItemId) {
        Member manager = getAuthenticatedAdmin(authentication);

        VoteItem voteItem = voteItemRepository.findById(voteItemId)
                .orElseThrow(() -> new IllegalArgumentException("투표 항목을 찾을 수 없습니다: " + voteItemId));

        Vote vote = voteItem.getVote();

        List<VoteStatus> voteStatuses = voteStatusRepository.findByVoteItem(voteItem);
        voteStatusRepository.deleteAll(voteStatuses);

        voteItemRepository.delete(voteItem);

        String currentVoteItemIds = vote.getVoteItemIds();
        List<Long> currentVoteItemIdsList = List.of(currentVoteItemIds.split(","))
                .stream().map(Long::parseLong).collect(Collectors.toList());
        currentVoteItemIdsList.remove(voteItemId);
        String updatedVoteItemIds = currentVoteItemIdsList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        vote.setVoteItemIds(updatedVoteItemIds);
        voteRepository.save(vote);
    }

    private Vote checkVoteEndTime(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));
        if (vote.getEndDateTime() != null && vote.getEndDateTime().isBefore(LocalDateTime.now())) {
            vote.setVoteEnded(true);
            voteRepository.save(vote);
            throw new IllegalArgumentException("이 투표는 이미 종료되었습니다.");
        }
        return vote;
    }

    private Member getAuthenticatedMember(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        if (member == null) {
            throw new IllegalArgumentException("해당 ID의 회원을 찾을 수 없습니다: " + studentId);
        }
        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoteResponseDTO> getAllVotes(Authentication authentication) {
        Member member = getAuthenticatedMember(authentication);

        return voteRepository.findAll().stream()
                .map(vote -> {
                    boolean hasVoted = voteStatusRepository.existsByVoteAndMember(vote, member);
                    return VoteResponseDTO.toResponseDTO(vote, hasVoted);
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VoteResponseDTO getVoteById(Authentication authentication, Long voteId) {
        Member member = getAuthenticatedMember(authentication);

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        List<VoteItemResponseDTO> voteItemResponseDTOs = vote.getVoteItems().stream()
                .map(voteItem -> {
                    List<String> voters = voteStatusRepository.findByVoteItem(voteItem).stream()
                            .map(voteStatus -> voteStatus.getMember().getStudentId())
                            .collect(Collectors.toList());

                    boolean hasVotedForThisItem = voters.contains(member.getStudentId());

                    return VoteItemResponseDTO.toResponseDTO(voteItem, hasVotedForThisItem, voters);
                })
                .collect(Collectors.toList());

        boolean hasVotedForAnyItem = voteItemResponseDTOs.stream().anyMatch(VoteItemResponseDTO::isHasVoted);

        return VoteResponseDTO.toResponseDTO(vote, hasVotedForAnyItem, voteItemResponseDTOs);
    }

    @Override
    @Transactional
    public void endVote(Authentication authentication, Long voteId) {
        Member manager = getAuthenticatedAdmin(authentication);

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new IllegalArgumentException("투표를 찾을 수 없습니다."));

        if (!vote.isVoteEnded()) {
            vote.setVoteEnded(true);
            voteRepository.save(vote);
        }
    }

    private Member getAuthenticatedAdmin(Authentication authentication) {
        Member member = getAuthenticatedMember(authentication);
        if (member.getUserRole() != UserRole.ROLE_ADMIN) {
            throw new IllegalArgumentException("관리자만 이 작업을 수행할 수 있습니다.");
        }
        return member;
    }
}
