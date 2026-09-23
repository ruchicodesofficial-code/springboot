package com.springboot.student_management_system.service;

import com.springboot.student_management_system.entity.RefreshToken;
import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.exception.InvalidRefreshTokenException;
import com.springboot.student_management_system.repository.RefreshTokenRepository;
import com.springboot.student_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final StudentRepository studentRepository;

    @Value("${refresh.expiration-days}")
    private long refreshTokenExpiryDays;

    public RefreshToken createRefreshToken(String email){
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(()->
                        new InvalidRefreshTokenException("Student not found with email: "+email));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setStudent(student);

        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(refreshTokenExpiryDays));

        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken verifyRefreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()->
                        new InvalidRefreshTokenException("Invalid refresh token"));

        if (refreshToken.isRevoked()){
            throw  new InvalidRefreshTokenException("Refresh token has been revoked");
        }
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }
        return refreshToken;
    }

    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldRefreshToken){
        oldRefreshToken.setRevoked(true);
        refreshTokenRepository.save(oldRefreshToken);
        return createRefreshToken(
                oldRefreshToken.getStudent().getEmail()
        );
    }

    @Transactional
    public void revokeRefreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()->
                    new InvalidRefreshTokenException("Invalid refresh token ")
                );
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}

