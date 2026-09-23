package com.springboot.student_management_system.service;

import com.springboot.student_management_system.dto.CourseRequestDTO;
import com.springboot.student_management_system.dto.CourseResponseDTO;
import com.springboot.student_management_system.entity.Course;
import com.springboot.student_management_system.exception.ResourceNotFoundException;
import com.springboot.student_management_system.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO requestDTO) {
        Course course = new Course();
        course.setCourseName(requestDTO.getCourseName());
        course.setDuration(requestDTO.getDuration());
        course.setFees(requestDTO.getFees());
        course.setInstructorName(requestDTO.getInstructorName());
        Course savedCourse = courseRepository.save(course);
        return mapToResponseDto(savedCourse);
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Course not found with id: "+id));

        return mapToResponseDto(course);
    }
    private CourseResponseDTO mapToResponseDto(Course course){
        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(course.getId());
        responseDTO.setCourseName(course.getCourseName());
        responseDTO.setDuration(course.getDuration());
        responseDTO.setFees(course.getFees());
        responseDTO.setInstructorName(course.getInstructorName());
        return responseDTO;
    }
}
