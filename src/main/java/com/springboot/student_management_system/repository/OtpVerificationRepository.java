package com.springboot.student_management_system.repository;

import com.springboot.student_management_system.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification,Long> {
    Optional<OtpVerification> findTopByEmailOrderByIdDesc(String email);
}
