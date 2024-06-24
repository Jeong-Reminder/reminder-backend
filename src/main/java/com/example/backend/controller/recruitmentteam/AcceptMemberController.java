package com.example.backend.controller.recruitmentteam;


import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.recruitmentteam.AcceptMemberRequestDTO;
import com.example.backend.dto.recruitmentteam.AcceptMemberResponseDTO;
import com.example.backend.service.recruitmentteam.AcceptMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accept-member")
public class AcceptMemberController {

    private final AcceptMemberService acceptMemberService;

    @PutMapping
    public ResponseDTO<Object> acceptMember(Authentication authentication,
                                            @RequestParam boolean accept,
                                            @RequestBody AcceptMemberRequestDTO acceptMemberRequestDTO) {
        AcceptMemberResponseDTO acceptMemberResponseDTO = acceptMemberService.acceptMember(authentication, accept, acceptMemberRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(acceptMemberResponseDTO)
                .build();
    }

}
