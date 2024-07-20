package com.example.backend.dto.announcement;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDTO {
    private Long id;
    private String orgNm; 
    private String saveNm;
    private String savedPath;
}