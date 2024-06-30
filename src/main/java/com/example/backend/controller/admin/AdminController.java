package com.example.backend.controller.admin;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.admin.MemberAdminResponseDTO;
import com.example.backend.dto.member.MemberResponseDTO;
import com.example.backend.service.admin.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/member-update")
    public ResponseDTO<Object> updateMember(Authentication authentication, @RequestParam("file") MultipartFile file)
            throws Exception {
        List<MemberAdminResponseDTO> memberAdminResponseDTOList = adminService.updateMember(authentication, file);

        return ResponseDTO.builder()
                .status(200)
                .data(memberAdminResponseDTOList)
                .build();
    }

    @DeleteMapping("/member-delete")
    public ResponseDTO<Object> deleteMember(Authentication authentication, @RequestBody List<String> studentIds) {
        List<MemberAdminResponseDTO> memberAdminResponseDTOList = adminService.deleteMember(authentication, studentIds);

        return ResponseDTO.builder()
                .status(200)
                .data(memberAdminResponseDTOList)
                .build();
    }
}
