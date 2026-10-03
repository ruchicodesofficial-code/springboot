package com.springboot.student_management_system.config;

import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@ruchicodes.com";
        if (studentRepository.existsByEmail(adminEmail)){
            System.out.println("Admin already exists.");
            return;
        }
        Student admin = new Student();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail(adminEmail);
        admin.setPassword(
                passwordEncoder.encode("Admin@12345")
        );
        admin.setRole("ADMIN");
        studentRepository.save(admin);
        System.out.println("=============================");
        System.out.println("Default admin created successfully!");
        System.out.println("Email : "+adminEmail);
        System.out.println("Password : Admin@12345");
        System.out.println("==============================");
    }
}
