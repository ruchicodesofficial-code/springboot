package com.springboot.student_management_system.controller;

import com.springboot.student_management_system.dto.CourseRequestDTO;
import com.springboot.student_management_system.dto.CourseResponseDTO;
import com.springboot.student_management_system.payload.ApiResponse;
import com.springboot.student_management_system.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponseDTO>> createCourse(@Valid @RequestBody
                                                                       CourseRequestDTO dto){
        CourseResponseDTO response = courseService.createCourse(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        "Course created successfully",
                        response
                ));

    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDTO>>> getAllCourses(){
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "All courses fetched successfully",
                        courseService.getAllCourses()

                )
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDTO>>getCourseById(@PathVariable Long id){

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Course fetched successfully",
                        courseService.getCourseById(id)
                )
        );
    }


}
