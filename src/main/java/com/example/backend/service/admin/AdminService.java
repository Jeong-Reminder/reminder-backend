package com.example.backend.service.admin;

import com.example.backend.dto.admin.MemberAdminResponseDTO;
import com.example.backend.dto.member.MemberRequestDTO;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    List<MemberAdminResponseDTO> updateMember(Authentication authentication, MultipartFile file) throws Exception;

    List<MemberAdminResponseDTO> deleteMember(Authentication authentication, List<String> studentIds);

    MemberAdminResponseDTO updateMemberInfo(Authentication authentication, MemberRequestDTO memberRequestDTO);

    MemberAdminResponseDTO insertAdmin(Authentication authentication, MemberRequestDTO memberRequestDTO);
}
