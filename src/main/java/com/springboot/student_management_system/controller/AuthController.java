package com.springboot.student_management_system.controller;

import com.springboot.student_management_system.dto.LoginRequest;
import com.springboot.student_management_system.dto.LoginResponse;
import com.springboot.student_management_system.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(
                authenticationService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestParam
                                                      String refreshToken){
        return ResponseEntity.ok(
                authenticationService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String refreshToken){
        authenticationService.logout(refreshToken);
        return ResponseEntity.ok(
                "Logout Successful!"
        );
    }
}
