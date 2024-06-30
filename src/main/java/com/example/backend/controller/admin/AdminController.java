package com.example.backend.controller.admin;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.admin.MemberAdminResponseDTO;
import com.example.backend.dto.member.MemberRequestDTO;
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

    @PutMapping("/member-update")
    public ResponseDTO<Object> updateMemberInfo(Authentication authentication, @RequestBody MemberRequestDTO memberRequestDTO) {
        MemberAdminResponseDTO memberAdminResponseDTO = adminService.updateMemberInfo(authentication, memberRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(memberAdminResponseDTO)
                .build();
    }

    @PostMapping("/admin-insert")
    public ResponseDTO<Object> insertAdmin(Authentication authentication, @RequestBody MemberRequestDTO memberRequestDTO) {
        MemberAdminResponseDTO memberAdminResponseDTO = adminService.insertAdmin(authentication, memberRequestDTO);

        return ResponseDTO.builder()
                .status(200)
                .data(memberAdminResponseDTO)
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

    @DeleteMapping("/recruitment-delete-all")
    public ResponseDTO<Object> deleteRecruitment(Authentication authentication) {
        adminService.deleteRecruitment(authentication);

        return ResponseDTO.builder()
                .status(200)
                .data("모집글 전체 삭제 완료")
                .build();
    }

    @DeleteMapping("/recruitment-delete")
    public ResponseDTO<Object> deleteCategoryRecruitment(Authentication authentication, @RequestParam String category) {
        adminService.deleteCategoryRecruitment(authentication, category);

        return ResponseDTO.builder()
                .status(200)
                .data(category+"모집글 삭제 완료")
                .build();
    }
}
