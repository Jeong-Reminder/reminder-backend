package com.example.backend.dto.announcement;


public enum AnnouncementCategory {
    SEASONAL_SYSTEM("계절제"),
    CORPORATE_TOUR("기업탐방"),
    CONTEST("경진대회"),
    ACADEMIC_ALL("학년공지");
    private final String label;
    AnnouncementCategory(String label) {
        this.label = label;
    }
    public String label() {
        return label;
    }
}

