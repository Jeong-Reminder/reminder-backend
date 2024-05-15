package com.example.backend.service;


import com.example.backend.dto.CustomMemberDetails;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomMemberDetailsService(MemberRepository memeberRepository) {

        this.memberRepository = memeberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        //DB에서 조회
        Member memberData = memberRepository.findByMembername(name);

        if (memberData != null) {
            return new CustomMemberDetails(memberData);
        }

        return null;
    }
}
