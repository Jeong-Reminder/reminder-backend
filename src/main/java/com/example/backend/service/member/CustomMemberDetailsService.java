package com.example.backend.service.member;

import com.example.backend.dto.member.CustomMemberDetails;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String studentId) throws UsernameNotFoundException {

        System.out.println("studentId : " + studentId);
        Member memberData = memberRepository.findByStudentId(studentId);

        if(memberData != null) {
            return new CustomMemberDetails(memberData);
        }
        return null;
    }
}
