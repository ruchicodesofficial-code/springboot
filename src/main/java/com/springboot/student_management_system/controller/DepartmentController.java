package com.springboot.student_management_system.controller;

import com.springboot.student_management_system.dto.DepartmentRequestDto;
import com.springboot.student_management_system.dto.DepartmentResponseDto;
import com.springboot.student_management_system.payload.ApiResponse;
import com.springboot.student_management_system.service.DepartmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DepartmentController {
    private final DepartmentService departmentService;
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(
            @Valid @RequestBody DepartmentRequestDto requestDto) {

        System.out.println(">>> CREATE DEPARTMENT CONTROLLER CALLED: "
                + requestDto.getDepartmentName());

        DepartmentResponseDto response =
                departmentService.createDepartment(requestDto);

        System.out.println(">>> DEPARTMENT CREATED: "
                + response.getDepartmentName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "Department created successfully",
                        response
                ));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartments(){
        List<DepartmentResponseDto> response = departmentService.getAllDepartments();
        ApiResponse<List<DepartmentResponseDto>> apiResponse =new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Departments fetched successfully",
                response
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentById(@PathVariable Long id){
        DepartmentResponseDto response = departmentService.getDepartmentById(id);
        ApiResponse<DepartmentResponseDto> apiResponse = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Department fetched successfully",
                response
        );
        return ResponseEntity.ok(apiResponse);
    }

}
