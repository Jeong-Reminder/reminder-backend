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
}
