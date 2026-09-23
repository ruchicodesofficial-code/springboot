package com.springboot.student_management_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private  final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(
                "Student Management System- OTP Verification"
        );
        message.setText(
                "Your OTP is:"+otp+
                        "\n\nThis OTP is valid for 5 minutes."
        );
        mailSender.send(message);
    }
}
