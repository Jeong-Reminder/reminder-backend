package com.example.backend.service.admin;

import com.example.backend.config.ExcelUtil;
import com.example.backend.dto.admin.MemberAdminResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.repository.member.MemberRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminImplService implements AdminService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<MemberAdminResponseDTO> updateMember(Authentication authentication, MultipartFile file) throws IOException {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        List<Member> newMembers = ExcelUtil.readMembersFromExcel(file.getInputStream());
        List<Member> oldMembers = memberRepository.findByUserRole(UserRole.ROLE_USER);

        Map<String, Member> oldMembersMap = oldMembers.stream()
                .collect(Collectors.toMap(Member::getStudentId, member -> member));

        for (Member newMember : newMembers) {
            Member oldMember = oldMembersMap.get(newMember.getStudentId());

            if (oldMember != null) {
                // Update existing member
                oldMember.setName(newMember.getName());
                oldMember.setLevel(newMember.getLevel());
                oldMember.setStatus(newMember.getStatus());
                memberRepository.save(oldMember);
                oldMembersMap.remove(newMember.getStudentId());
            } else {
                // Save new member with default password
                newMember.setPassword(bCryptPasswordEncoder.encode("1111"));
                memberRepository.save(newMember);
            }
        }

        // Delete members not in newMembers list
        for (Member oldMember : oldMembersMap.values()) {
            memberRepository.delete(oldMember);
        }

        List<Member> members = memberRepository.findAll();
        List<MemberAdminResponseDTO> memberAdminResponseDTOList = MemberAdminResponseDTO.toResponseDTOList(members);

        return memberAdminResponseDTOList ;
    }
}
