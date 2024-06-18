package com.example.backend.service.member;

import com.example.backend.dto.member.TechStackRequestDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.Profile;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.member.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    public void techStack(String studentId, TechStackRequestDTO techStackRequestDTO) {
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Profile profile = member.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setMember(member);
        }
        profile.setGithubLink(techStackRequestDTO.getGithubLink());
        profile.setDevelopmentField(techStackRequestDTO.getDevelopmentField());
        profile.setDevelopmentTool(techStackRequestDTO.getDevelopmentTool());

        profileRepository.save(profile);
    }

}
