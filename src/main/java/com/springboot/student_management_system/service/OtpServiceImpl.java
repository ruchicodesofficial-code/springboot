package com.springboot.student_management_system.service;

import com.springboot.student_management_system.entity.OtpVerification;
import com.springboot.student_management_system.repository.OtpVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService{
    private final OtpVerificationRepository otpRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();
    
    @Override
    public void generateAndSendOtp(String email) {
        String otp = String.format(
            "%06d",
                secureRandom.nextInt(1_000_000)
        );

        OtpVerification verification = new OtpVerification();
        verification.setEmail(email);
        verification.setOtp(otp);
        verification.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );
        verification.setVerified(false);
        otpRepository.save(verification);
        emailService.sendOtpEmail(email,otp);
        
    }

    @Override
    public void verifyOtp(String email, String otp) {
        OtpVerification verification = otpRepository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(()->
                        new RuntimeException("Otp not found"));
        if (verification.isVerified()){
            throw new RuntimeException(
                    "Otp already verified"
            );
        }
        if (LocalDateTime.now().isAfter(verification.getExpiresAt())){
            throw new RuntimeException("Otp expired");
        }
        if(!verification.getOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }
        verification.setVerified(true);
        otpRepository.save(verification);
    }
}
