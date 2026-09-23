package com.springboot.student_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDTO {
    @NotBlank(message = "Course name is required")
    private String courseName;
    @NotBlank(message = "Duration is required")
    private String duration;
    @NotNull(message = "Fees is required")
    private Double fees;
    @NotBlank(message = "Instructor name is required")
    private String instructorName;
}
