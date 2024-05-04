package com.example.backend.model.entity;

import com.example.backend.dto.UserRole;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String student_Id;
    private String password;
    private String name;
    private Integer level;
    private String status;

    private String role;

}
