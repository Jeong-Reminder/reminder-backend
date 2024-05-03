package com.example.backend;


import com.example.backend.dto.UserRole;
import com.example.backend.model.entity.User;
import com.example.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MakeInitData {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @PostConstruct
    public void makeAdminAndUser() {
        User admin1 = User.builder()
                .student_Id("admin1")
                .password("1234")
                .name("관리자1")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin1);

        User user1 = User.builder()
                .student_Id("user1")
                .password("1234")
                .name("User1")
                .role(UserRole.USER)
                .build();
        userRepository.save(user1);

        User admin2 = User.builder()
                .student_Id("admin2")
                .password(encoder.encode("1234"))
                .name("관리자")
                .role(UserRole.ADMIN)
                .build();
        userRepository.save(admin2);

        User user2 = User.builder()
                .student_Id("user")
                .password(encoder.encode("1234"))
                .name("유저1")
                .role(UserRole.USER)
                .build();
        userRepository.save(user2);
    }
}
