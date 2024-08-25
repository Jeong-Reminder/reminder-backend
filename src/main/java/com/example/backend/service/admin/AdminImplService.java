package com.example.backend.service.admin;

import com.example.backend.config.ExcelUtil;
import com.example.backend.dto.admin.MemberAdminResponseDTO;
import com.example.backend.dto.member.MemberRequestDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.UserRole;
import com.example.backend.model.repository.announcement.ContestCategoryRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.RecruitmentRepository;
import com.example.backend.model.repository.recruitmentteam.TeamRepository;
import jakarta.transaction.Transactional;
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

    private final TeamRepository teamRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ContestCategoryRepository contestCategoryRepository;

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

    @Override
    public MemberAdminResponseDTO updateMemberInfo(Authentication authentication, MemberRequestDTO memberRequestDTO) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        Member member = memberRepository.findByStudentId(memberRequestDTO.getStudentId());
        member.setName(memberRequestDTO.getName());
        member.setLevel(memberRequestDTO.getLevel());
        member.setStatus(memberRequestDTO.getStatus());
        member.setUserRole(memberRequestDTO.getUserRole());

        Member saveMember = memberRepository.save(member);
        MemberAdminResponseDTO memberAdminResponseDTO = MemberAdminResponseDTO.toResponseDTO(saveMember);

        return memberAdminResponseDTO;
    }

    @Override
    public MemberAdminResponseDTO insertAdmin(Authentication authentication, MemberRequestDTO memberRequestDTO) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        Member member = Member.builder()
                .studentId(memberRequestDTO.getStudentId())
                .name(memberRequestDTO.getName())
                .level(memberRequestDTO.getLevel())
                .status(memberRequestDTO.getStatus())
                .userRole(memberRequestDTO.getUserRole())
                .password(bCryptPasswordEncoder.encode("1111"))
                .build();

        Member saveMember = memberRepository.save(member);
        MemberAdminResponseDTO memberAdminResponseDTO = MemberAdminResponseDTO.toResponseDTO(saveMember);

        return memberAdminResponseDTO;
    }

    @Override
    public List<MemberAdminResponseDTO> getMembers(Authentication authentication) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        List<Member> members = memberRepository.findAll();
        List<MemberAdminResponseDTO> memberAdminResponseDTOList = MemberAdminResponseDTO.toResponseDTOList(members);

        return memberAdminResponseDTOList;
    }

    @Override
    public List<MemberAdminResponseDTO> deleteMember(Authentication authentication, List<String> studentIds) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        for(String deleteStudentId : studentIds) {
            Member member = memberRepository.findByStudentId(deleteStudentId);
            memberRepository.delete(member);
        }

        List<Member> members = memberRepository.findAll();
        List<MemberAdminResponseDTO> memberAdminResponseDTOList = MemberAdminResponseDTO.toResponseDTOList(members);

        return memberAdminResponseDTOList;
    }

    @Override
    @Transactional
    public void deleteRecruitment(Authentication authentication) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        contestCategoryRepository.deleteAll();
        recruitmentRepository.deleteAll();
    }

    @Override
    public void deleteCategoryRecruitment(Authentication authentication, String category) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        contestCategoryRepository.deleteByContestCategoryName(category);
        recruitmentRepository.deleteByRecruitmentCategory(category);
    }

    @Override
    @Transactional
    public void deleteTeam(Authentication authentication) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        teamRepository.deleteAll();
    }

    @Override
    public void deleteCategoryTeam(Authentication authentication, String category) {
        String studentId = authentication.getName();

        Member adminMember = memberRepository.findByStudentId(studentId);

        if (adminMember.getUserRole().equals(UserRole.ROLE_USER)) {
            throw new IllegalArgumentException("관리자 권한이 없습니다.");
        }

        teamRepository.deleteByTeamCategory(category);
    }
}
