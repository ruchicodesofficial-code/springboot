package com.springboot.student_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class StudentRequestDto {
    @NotBlank(message = "First name is required")
    @Size(min=2,max=30,message = "First name must be between 2 and 30 character")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min=2,max=50,message = "Last name must be between 2 and 50 character")
    private String lastName;
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min=8, message="Password must contain at least 8 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message ="Password must contain at least 8 characters, one uppercase letter,one lowercase letter,one number and one special character."


    )
    private  String password;
    private AddressRequestDTO address;
    @NotNull(message = "Department Id is required")
    private Long departmentId;
    @NotEmpty(message = "At least one course is required")
    private List<Long> courseIds;
}
