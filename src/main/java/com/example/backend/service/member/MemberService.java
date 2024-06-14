package com.example.backend.service.member;

import com.example.backend.dto.member.TechStackDTO;
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

    public void techStack(String studentId, TechStackDTO techStackDTO) {
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Profile profile = member.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setMember(member);
        }
        profile.setGithubLink(techStackDTO.getGithubLink());
        profile.setDevelopmentField(techStackDTO.getDevelopmentField());
        profile.setDevelopmentTool(techStackDTO.getDevelopmentTool());

        profileRepository.save(profile);
    }

}
