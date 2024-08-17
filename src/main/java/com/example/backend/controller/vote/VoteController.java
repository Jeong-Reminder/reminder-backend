package com.example.backend.controller.vote;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.vote.*;
import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.repository.announcement.AnnouncementRepository;
import com.example.backend.service.vote.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final AnnouncementRepository announcementRepository;

    @PostMapping("/{announcementId}")
    public ResponseEntity<ResponseDTO<VoteResponseDTO>> createVote(Authentication authentication,
                                                                   @PathVariable Long announcementId,
                                                                   @RequestBody VoteRequestDTO voteRequestDTO) {
        try {
            Announcement announcement = announcementRepository.findById(announcementId)
                    .orElseThrow(() -> new IllegalArgumentException("Announcement not found with ID: " + announcementId));

            VoteResponseDTO responseDTO = VoteResponseDTO.toResponseDTO(
                    voteService.createVote(authentication, voteRequestDTO, announcement),
                    false
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDTO.<VoteResponseDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .data(responseDTO)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<VoteResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{voteId}")
    public ResponseEntity<ResponseDTO<String>> updateVote(Authentication authentication,
                                                          @PathVariable Long voteId,
                                                          @RequestBody VoteRequestDTO voteRequestDTO) {
        try {
            VoteResponseDTO updatedVote = voteService.updateVote(authentication, voteId, voteRequestDTO);
            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.OK.value())
                            .data("투표가 성공적으로 업데이트되었습니다!")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<ResponseDTO<String>> deleteVote(Authentication authentication,
                                                          @PathVariable Long voteId) {
        try {
            voteService.deleteVote(authentication, voteId);
            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.OK.value())
                            .data("투표가 성공적으로 삭제되었습니다!")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<VoteResponseDTO>>> getAllVotes(Authentication authentication) {
        List<VoteResponseDTO> votes = voteService.getAllVotes(authentication);
        return ResponseEntity.ok(
                ResponseDTO.<List<VoteResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .data(votes)
                        .build()
        );
    }

    // 특정 투표 조회
    @GetMapping("/{voteId}")
    public ResponseEntity<ResponseDTO<VoteResponseDTO>> getVoteById(Authentication authentication,
                                                                    @PathVariable Long voteId) {
        try {
            VoteResponseDTO vote = voteService.getVoteById(authentication, voteId);
            return ResponseEntity.ok(
                    ResponseDTO.<VoteResponseDTO>builder()
                            .status(HttpStatus.OK.value())
                            .data(vote)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<VoteResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    // 투표 항목 추가
    @PostMapping("/{voteId}/items")
    public ResponseEntity<ResponseDTO<String>> addVoteItem(Authentication authentication,
                                                           @PathVariable Long voteId,
                                                           @RequestBody VoteItemRequestDTO voteItemRequestDTO) {
        try {
            VoteItemResponseDTO responseDTO = voteService.addVoteItem(authentication, voteId, voteItemRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.CREATED.value())
                            .data("투표 항목이 성공적으로 추가되었습니다!")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/{voteId}/end")
    public ResponseEntity<ResponseDTO<String>> endVote(Authentication authentication,
                                                       @PathVariable Long voteId) {
        try {
            voteService.endVote(authentication, voteId);
            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.OK.value())
                            .data("투표가 성공적으로 종료되었습니다!")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/{voteId}/recast")
    public ResponseEntity<ResponseDTO<String>> recastVote(Authentication authentication,
                                                          @PathVariable Long voteId,
                                                          @RequestBody List<Long> voteItemIds) {
        try {
            voteService.recastVote(authentication, voteId, voteItemIds);
            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.OK.value())
                            .data("투표가 성공적으로 재실행되었습니다!")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/{voteId}/vote")
    public ResponseEntity<ResponseDTO<String>> vote(Authentication authentication,
                                                    @PathVariable Long voteId,
                                                    @RequestBody VoteItemRequestDTO voteItemRequestDTO) {
        try {
            voteService.vote(authentication, voteId, voteItemRequestDTO.getVoteItemIds());
            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.OK.value())
                            .data("투표가 성공적으로 완료되었습니다!")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/items/{voteItemId}")
    public ResponseEntity<ResponseDTO<String>> deleteVoteItem(Authentication authentication,
                                                              @PathVariable Long voteItemId) {
        try {
            voteService.deleteVoteItem(authentication, voteItemId);
            return ResponseEntity.ok(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.OK.value())
                            .data("투표 항목이 성공적으로 삭제되었습니다!")
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<String>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }
}
