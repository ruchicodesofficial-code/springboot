package com.springboot.student_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentRequestDto {
    @NotBlank(message = "Department name is required")
    @Size(max = 100,message = "Department name cannot exceed 100 characters")
    private String departmentName;
}
