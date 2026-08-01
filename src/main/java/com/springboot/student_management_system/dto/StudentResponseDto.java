package com.springboot.student_management_system.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String departmentName;
    private AddressResponseDTO address;
    private List<CourseResponseDTO> courses;


}
