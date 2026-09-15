package com.library.lms.config;

import com.library.lms.model.Role;
import com.library.lms.model.User;
import com.library.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Khởi tạo tài khoản ADMIN mặc định khi ứng dụng chạy lần đầu (nếu chưa có).
 * Tài khoản mặc định: admin / admin123 (nên đổi mật khẩu sau khi triển khai).
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@library.com")
                    .fullName("Quản trị viên hệ thống")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println(">>> Đã tạo tài khoản ADMIN mặc định: username=admin, password=admin123");
        }
    }
}
