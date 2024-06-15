package com.example.backend.dto.recruitmentteam;

import com.example.backend.model.entity.recruitmentteam.Recruitment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecruitmentResponseDTO {
    private Long id;
    private String recruitmentCategory;
    private String recruitmentTitle;
    private String recruitmentContent;
    private int studentCount;
    private String hopeField;
    private String kakaoUrl;
    private boolean recruitmentStatus;
    private LocalDateTime createdTime;
    private LocalDateTime endTime;
    private Long announcementId;

    public static RecruitmentResponseDTO toResponseDTO(Recruitment recruitment) {
        return RecruitmentResponseDTO.builder()
                .id(recruitment.getId())
                .recruitmentCategory(recruitment.getRecruitmentCategory())
                .recruitmentTitle(recruitment.getRecruitmentTitle())
                .recruitmentContent(recruitment.getRecruitmentContent())
                .studentCount(recruitment.getStudentCount())
                .hopeField(recruitment.getHopeField())
                .kakaoUrl(recruitment.getKakaoUrl())
                .recruitmentStatus(recruitment.isRecruitmentStatus())
                .createdTime(recruitment.getCreatedTime())
                .endTime(recruitment.getEndTime())
                .announcementId(recruitment.getAnnouncement().getId())
                .build();
    }

    public static List<RecruitmentResponseDTO> toResponseDTOList(List<Recruitment> recruitmentList) {
        return recruitmentList.stream()
                .map(RecruitmentResponseDTO::toResponseDTO)
                .toList();
    }
}
