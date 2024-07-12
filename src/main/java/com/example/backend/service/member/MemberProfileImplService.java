package com.example.backend.service.member;

import com.example.backend.dto.member.MemberProfileRequestDTO;
import com.example.backend.dto.member.MemberProfileResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.repository.member.MemberProfileRepository;
import com.example.backend.model.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileImplService implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final MemberRepository memberRepository;

    @Override
    public MemberProfileResponseDTO createProfile(Authentication authentication,
                                                  MemberProfileRequestDTO memberProfileRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        MemberProfile memberProfile = memberProfileRequestDTO.toEntity(member);

        MemberProfileResponseDTO memberProfileResponseDTO = MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.save(memberProfile));

        return memberProfileResponseDTO;
    }

    @Override
    public MemberProfileResponseDTO updateProfile(Authentication authentication,
                                                  MemberProfileRequestDTO memberProfileRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        MemberProfile memberProfile = memberProfileRepository.findByMemberId(member.getId());

        if (!memberProfile.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("프로필 수정권한이 없습니다.");
        }

        memberProfile.setDevelopmentField(memberProfileRequestDTO.getDevelopmentField());
        memberProfile.setDevelopmentTool(memberProfileRequestDTO.getDevelopmentTool());
        memberProfile.setGithubLink(memberProfileRequestDTO.getGithubLink());
        memberProfile.setHopeJob(memberProfileRequestDTO.getHopeJob());

        MemberProfileResponseDTO memberProfileResponseDTO = MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.save(memberProfile));

        return memberProfileResponseDTO;
    }

    @Override
    public MemberProfileResponseDTO getMemberProfile(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        MemberProfileResponseDTO memberProfileResponseDTO = MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.findByMemberId(member.getId()));

        return memberProfileResponseDTO;
    }

    @Override
    public MemberProfileResponseDTO getMemberProfileByMemberId(Long memberId) {
        MemberProfileResponseDTO memberProfileResponseDTO = MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.findByMemberId(memberId));

        return memberProfileResponseDTO;
    }
}
