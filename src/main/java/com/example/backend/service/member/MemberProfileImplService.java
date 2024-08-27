package com.example.backend.service.member;

import com.example.backend.dto.member.MemberProfileRequestDTO;
import com.example.backend.dto.member.MemberProfileResponseDTO;
import com.example.backend.dto.recruitmentteam.TeamResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.member.MemberProfile;
import com.example.backend.model.entity.recruitmentteam.Team;
import com.example.backend.model.entity.recruitmentteam.TeamMember;
import com.example.backend.model.repository.member.MemberProfileRepository;
import com.example.backend.model.repository.member.MemberRepository;
import com.example.backend.model.repository.recruitmentteam.TeamMemberRepository;
import com.example.backend.model.repository.recruitmentteam.TeamRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileImplService implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Override
    public MemberProfileResponseDTO createProfile(Authentication authentication,
                                                  MemberProfileRequestDTO memberProfileRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        MemberProfile memberProfile = memberProfileRequestDTO.toEntity(member);

        return MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.save(memberProfile));
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

        return MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.save(memberProfile));
    }

    @Override
    public MemberProfileResponseDTO getMemberProfile(Authentication authentication) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);
        List<TeamMember> teamMembers = teamMemberRepository.findByMemberId(member.getId());

        MemberProfileResponseDTO memberProfileResponseDTO = MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.findByMemberId(member.getId()));
        if(teamMembers.isEmpty()) {
            memberProfileResponseDTO.setTeam(null);
        }else{
            List<MemberProfile> profiles = new ArrayList<>();
            for(TeamMember teamMember : teamMembers){
                Team team = teamRepository.findById(teamMember.getTeam().getId()).get();
                profiles.add(teamMember.getMember().getMemberProfile());

                memberProfileResponseDTO.getTeam().add(TeamResponseDTO.toResponseDTO(profiles,team));
            }

        }
        return memberProfileResponseDTO;
    }

    @Override
    public MemberProfileResponseDTO getMemberProfileByMemberId(Long memberId) {

        List<TeamMember> teamMembers = teamMemberRepository.findByMemberId(memberId);

        MemberProfileResponseDTO memberProfileResponseDTO = MemberProfileResponseDTO.toResponseDTO(memberProfileRepository.findByMemberId(memberId));
        if(teamMembers.isEmpty()) {
            memberProfileResponseDTO.setTeam(null);
        }else{
            List<MemberProfile> profiles = new ArrayList<>();
            for(TeamMember teamMember : teamMembers){
                Team team = teamRepository.findById(teamMember.getTeam().getId()).get();
                profiles.add(teamMember.getMember().getMemberProfile());

                memberProfileResponseDTO.getTeam().add(TeamResponseDTO.toResponseDTO(profiles,team));
            }

        }
        return memberProfileResponseDTO;
    }
}
