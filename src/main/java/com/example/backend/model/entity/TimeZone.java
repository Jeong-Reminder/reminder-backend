package com.example.backend.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class TimeZone {
    @Column(updatable = false)
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @PrePersist
    public void createTime(){
        LocalDateTime now = LocalDateTime.now().withNano(0);
        this.createdTime = now;
        this.updatedTime = now;
    }

    @PreUpdate
    public void updateTime(){
        this.updatedTime = LocalDateTime.now().withNano(0);
    }
}
