package com.springboot.student_management_system.service;


public interface EmailService {
    void sendOtpEmail(String toEmail,String otp);
}
