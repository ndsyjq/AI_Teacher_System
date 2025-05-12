package com.example.ai_teacher_system_back.config;

import com.example.ai_teacher_system_back.entity.User;
import com.example.ai_teacher_system_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 数据初始化类
 * 用于在应用启动时初始化必要的数据
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 初始化管理员用户
        initAdminUser();
    }

    /**
     * 初始化管理员用户
     */
    private void initAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            log.info("创建管理员用户");
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRealName("系统管理员");
            admin.setEmail("admin@example.com");
            admin.setStatus(true);
            admin.setCreateTime(LocalDateTime.now());
            admin.setUpdateTime(LocalDateTime.now());
            userRepository.save(admin);
        }
    }
}