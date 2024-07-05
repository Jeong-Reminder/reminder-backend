package com.example.backend.model.repository.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.AcceptMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcceptMemberRepository extends JpaRepository<AcceptMember, Long> {
    List<AcceptMember> findByMemberId(Long memberId);
}
