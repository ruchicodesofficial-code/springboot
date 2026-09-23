package com.springboot.student_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentProjectionDTO {
    private String firstName;
    private String lastName;
    private String email;
}
