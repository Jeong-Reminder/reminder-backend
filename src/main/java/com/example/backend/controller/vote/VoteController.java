package com.example.backend.controller.vote;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.vote.*;
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

    @PostMapping("/")
    public ResponseEntity<ResponseDTO<VoteResponseDTO>> createVote(Authentication authentication,@RequestBody VoteRequestDTO voteRequestDTO) {
        try {
            VoteResponseDTO responseDTO = VoteResponseDTO.toResponseDTO(voteService.createVote(authentication,voteRequestDTO));
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
    public ResponseEntity<ResponseDTO<VoteResponseDTO>> updateVote(Authentication authentication,@PathVariable Long voteId,
                                                                   @RequestBody VoteRequestDTO voteRequestDTO) {
        try {
            VoteResponseDTO responseDTO = voteService.updateVote(authentication,voteId, voteRequestDTO);
            return ResponseEntity.ok(
                    ResponseDTO.<VoteResponseDTO>builder()
                            .status(HttpStatus.OK.value())
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

    @DeleteMapping("/{voteId}")
    public ResponseEntity<ResponseDTO<Void>> deleteVote(Authentication authentication,@PathVariable Long voteId) {
        try {
            voteService.deleteVote(authentication,voteId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<Void>builder()
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

    @GetMapping("/{voteId}")
    public ResponseEntity<ResponseDTO<VoteResponseDTO>> getVoteById(Authentication authentication,@PathVariable Long voteId) {
        try {
            VoteResponseDTO vote = voteService.getVoteById(authentication,voteId);
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

    @PostMapping("/{voteId}/items")
    public ResponseEntity<ResponseDTO<VoteItemResponseDTO>> addVoteItem(Authentication authentication,@PathVariable Long voteId,
                                                                        @RequestBody VoteItemRequestDTO voteItemRequestDTO) {
        try {
            VoteItemResponseDTO responseDTO = voteService.addVoteItem(authentication,voteId, voteItemRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDTO.<VoteItemResponseDTO>builder()
                            .status(HttpStatus.CREATED.value())
                            .data(responseDTO)
                            .build()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<VoteItemResponseDTO>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/{voteId}/end")
    public ResponseEntity<ResponseDTO<Void>> endVote(Authentication authentication,@PathVariable Long voteId) {
        try {
            voteService.endVote(authentication,voteId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }


    @PostMapping("/{voteId}/recast")
    public ResponseEntity<ResponseDTO<Void>> recastVote(Authentication authentication,
                                                        @PathVariable Long voteId,
                                                        @RequestBody List<Long> voteItemIds) {
        try {
            voteService.recastVote(authentication,voteId, voteItemIds);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDTO.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .error(e.getMessage())
                            .build()
            );
        }
    }
    @PostMapping("/{voteId}/vote/{voteItemId}")
    public ResponseEntity<Void> vote(Authentication authentication, @PathVariable Long voteId, @PathVariable Long voteItemId) {
        voteService.vote(authentication, voteId, voteItemId);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/items/{voteItemId}")
    public ResponseEntity<Void> deleteVoteItem(Authentication authentication, @PathVariable Long voteItemId) {
        voteService.deleteVoteItem(authentication, voteItemId);
        return ResponseEntity.noContent().build();
    }
}
