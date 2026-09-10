package com.springboot.student_management_system.security;

import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByEmail(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("Student not found with email: "+username));

        return User.builder()
                .username(student.getEmail())
                .password(student.getPassword())
                .roles(student.getRole())
                .build();


    }
}
