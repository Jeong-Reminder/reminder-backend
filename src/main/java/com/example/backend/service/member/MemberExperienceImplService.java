package com.example.backend.service.member;

import com.example.backend.dto.member.MemberExperienceRequestDTO;
import com.example.backend.dto.member.MemberExperienceResponseDTO;
import com.example.backend.model.entity.member.MemberExperience;
import com.example.backend.model.repository.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.example.backend.model.repository.member.MemberExperienceRepository;
import com.example.backend.model.entity.member.Member;

@Service
@RequiredArgsConstructor
public class MemberExperienceImplService implements MemberExperienceService {

    private final MemberExperienceRepository memberExperienceRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<MemberExperienceResponseDTO> createMemberExperience(Authentication authentication,
                                                                    MemberExperienceRequestDTO memberExperienceRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        MemberExperienceResponseDTO.toResponseDTO(memberExperienceRepository.save(memberExperienceRequestDTO.toEntity(member)));

        List<MemberExperience> memberExperienceList = memberExperienceRepository.findAllByMember(member);

        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = MemberExperienceResponseDTO.toResponseDTOList(memberExperienceList);

        return memberExperienceResponseDTOList;
    }

    @Override
    public List<MemberExperienceResponseDTO> createMemberExperienceList(Authentication authentication,
                                                                        List<MemberExperienceRequestDTO> memberExperienceRequestDTOList) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        List<MemberExperience> memberExperienceList = memberExperienceRequestDTOList.stream()
                .map(memberExperienceRequestDTO -> memberExperienceRequestDTO.toEntity(member))
                .toList();

        memberExperienceRepository.saveAll(memberExperienceList);

        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = MemberExperienceResponseDTO.toResponseDTOList(memberExperienceList);

        return memberExperienceResponseDTOList;
    }

    @Override
    public List<MemberExperienceResponseDTO> updateMemberExperience(Authentication authentication,
                                                                    MemberExperienceRequestDTO memberExperienceRequestDTO,
                                                                    Long memberExperienceId) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        MemberExperience memberExperience = memberExperienceRepository.findById(memberExperienceId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경험을 찾을 수 없습니다."));

        if (!memberExperience.getMember().equals(member)) {
            throw new IllegalArgumentException("해당 경험을 수정할 수 없습니다.");
        }

        memberExperience.setExperienceContent(memberExperienceRequestDTO.getExperienceContent());
        memberExperience.setExperienceDate(memberExperienceRequestDTO.getExperienceDate());
        memberExperience.setExperienceGithub(memberExperienceRequestDTO.getExperienceGithub());
        memberExperience.setExperienceJob(memberExperienceRequestDTO.getExperienceJob());
        memberExperience.setExperienceName(memberExperienceRequestDTO.getExperienceName());
        memberExperience.setExperienceRole(memberExperienceRequestDTO.getExperienceRole());

        memberExperienceRepository.save(memberExperience);

        List<MemberExperience> memberExperienceList = memberExperienceRepository.findAllByMember(member);

        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = MemberExperienceResponseDTO.toResponseDTOList(memberExperienceList);

        return memberExperienceResponseDTOList;
    }

    @Override
    public List<MemberExperienceResponseDTO> getMemberExperience(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        List<MemberExperience> memberExperienceList = memberExperienceRepository.findAllByMember(member);

        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = MemberExperienceResponseDTO.toResponseDTOList(memberExperienceList);

        return memberExperienceResponseDTOList;
    }

    @Override
    public List<MemberExperienceResponseDTO> getMemberExperienceByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        List<MemberExperience> memberExperienceList = memberExperienceRepository.findAllByMember(member);

        List<MemberExperienceResponseDTO> memberExperienceResponseDTOList = MemberExperienceResponseDTO.toResponseDTOList(memberExperienceList);

        if(memberExperienceResponseDTOList.isEmpty()) {
            return null;
        }
        return memberExperienceResponseDTOList;
    }
}
