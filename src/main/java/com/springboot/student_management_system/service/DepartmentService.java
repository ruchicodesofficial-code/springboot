package com.springboot.student_management_system.service;

import com.springboot.student_management_system.dto.DepartmentRequestDto;
import com.springboot.student_management_system.dto.DepartmentResponseDto;
import com.springboot.student_management_system.entity.Department;

import java.util.List;

public interface DepartmentService {
    DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto);
    List<DepartmentResponseDto> getAllDepartments();
    DepartmentResponseDto getDepartmentById(Long id);
}
