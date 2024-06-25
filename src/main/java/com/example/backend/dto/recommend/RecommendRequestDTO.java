package com.example.backend.dto.recommend;

import com.example.backend.model.entity.announcement.Announcement;
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
    private Long memberId;
    public Recommend toEntity(Announcement announcement, Member member) {
        Recommend recommend = new Recommend();
        recommend.setAnnouncement(announcement);
        recommend.setMember(member);
        recommend.setStatus(false);
        return recommend;
    }
}
