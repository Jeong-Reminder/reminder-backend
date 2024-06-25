package com.example.backend.service.member;

import com.example.backend.dto.member.ChangePasswordRequestDTO;
import com.example.backend.dto.member.MemberRequestDTO;
import com.example.backend.dto.member.MemberResponseDTO;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberImplService implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public MemberResponseDTO signup(MemberRequestDTO memberRequestDTO) {

        boolean isExist = memberRepository.existsByStudentId(memberRequestDTO.getStudentId());

        if(isExist) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        Member member = memberRepository.save(memberRequestDTO.toEntity(bCryptPasswordEncoder.encode(memberRequestDTO.getPassword())));

        return MemberResponseDTO.builder()
                .name(member.getName())
                .level(member.getLevel())
                .status(member.getStatus())
                .userRole(member.getUserRole())
                .techStack(null)
                .build();
    }

    @Override
    public MemberResponseDTO changePassword(Authentication authentication,
                                            ChangePasswordRequestDTO changePasswordRequestDTO) {
        String studentId = authentication.getName();
        Member member = memberRepository.findByStudentId(studentId);

        if(!bCryptPasswordEncoder.matches(changePasswordRequestDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        member.setPassword(bCryptPasswordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        Member saveMember = memberRepository.save(member);

        return MemberResponseDTO.builder()
                .name(saveMember.getName())
                .level(saveMember.getLevel())
                .status(saveMember.getStatus())
                .userRole(saveMember.getUserRole())
                .techStack(null)
                .build();
    }
}
