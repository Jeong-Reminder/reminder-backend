package com.example.backend.service;

import com.example.backend.dto.LoginRequest;
import com.example.backend.model.entity.Member;
import com.example.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor

public class LoginService {

    private final MemberRepository memberRepository;

//    public Member login(String student_Id, String password){
//        Member member= memberRepository.findByStudent_Id(student_Id);
//        if(member.getPassword().equals(password)){
//            return member;
//        }
//        else{
//            return null;
//        }
//    }

    public Member login(LoginRequest req) {
        Optional<Member> optionalMember = memberRepository.findByStudentId(req.getStudentId());

        // loginId와 일치하는 User가 없으면 null return
        if (optionalMember.isEmpty()) {
            return null;
        }

        Member member = optionalMember.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if (!member.getPassword().equals(req.getPassword())) {
            return null;
        }

        return member;
    }

    public Member getLoginMemberById(Long memberId) {
        if(memberId == null) return null;

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) return null;

        return optionalMember.get();
        }
    }


