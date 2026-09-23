package com.springboot.student_management_system.service;

public interface OtpService {
    void generateAndSendOtp(String email);
    void verifyOtp(String email,String otp);
}
