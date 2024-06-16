package com.example.backend.dto;

import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseListDTO<T> {
    private String error;
    private int status;
    private List<T> data;
}
