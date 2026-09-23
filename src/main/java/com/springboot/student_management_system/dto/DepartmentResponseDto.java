package com.springboot.student_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponseDto {
    private Long id;
    private String departmentName;
    private List<StudentResponseDto> students;
}
