package com.example.backend.dto;

import com.example.backend.dto.recruitmentteam.RecruitmentResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
    private String error;
    private int status;
    private T data;

    public ResponseDTO(int status, RecruitmentResponseDTO recruitmentResponseDTO) {
    }

    public ResponseDTO(int status, String s) {
    }
}
