package com.springboot.student_management_system.controller;

import com.springboot.student_management_system.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth/otp")
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email){
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok(
                "OTP send successfully"
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp){
        otpService.verifyOtp(email,otp);
        return ResponseEntity.ok(
                "Otp verified successfully"
        );
    }
}
