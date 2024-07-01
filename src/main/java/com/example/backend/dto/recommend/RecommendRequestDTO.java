package com.example.backend.dto.recommend;

import com.example.backend.model.entity.announcement.Announcement;
import com.example.backend.model.entity.member.Member;
import com.example.backend.model.entity.recommend.Recommend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RecommendRequestDTO {
    private Long announcementId;

    public Recommend toEntity(Announcement announcement, Member member) {
        Recommend recommend = new Recommend();
        recommend.setAnnouncement(announcement);
        recommend.setMember(member);
        recommend.setStatus(true); // 기본적으로 좋아요 상태로 설정
        return recommend;
    }
}
