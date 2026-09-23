package com.springboot.student_management_system.service;

import com.springboot.student_management_system.dto.CourseRequestDTO;
import com.springboot.student_management_system.dto.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    CourseResponseDTO createCourse(CourseRequestDTO requestDTO);
    List<CourseResponseDTO> getAllCourses();
    CourseResponseDTO getCourseById(Long id);

}
