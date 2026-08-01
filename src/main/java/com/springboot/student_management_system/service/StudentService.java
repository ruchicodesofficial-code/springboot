package com.springboot.student_management_system.service;

import com.springboot.student_management_system.dto.StudentProjectionDTO;
import com.springboot.student_management_system.dto.StudentRequestDto;
import com.springboot.student_management_system.dto.StudentResponseDto;
import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.projection.StudentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface StudentService {
    StudentResponseDto saveStudent(StudentRequestDto dto);
    List<StudentResponseDto> getAllStudents();
    StudentResponseDto getStudentById(Long id);
    void deleteStudent(Long id);
    StudentResponseDto updateStudent(Long id,StudentRequestDto dto);
    StudentResponseDto patchStudent(Long id,StudentRequestDto dto);
    StudentResponseDto getStudentByEmail(String email);
    Long getStudentCountByCourse(String courseName);
    void deleteStudentByEmail(String email);
    List<StudentResponseDto> getStudentByFirstNameAndCourse(String firstName,String course);
    List<StudentResponseDto> findByFirstNameContaining(String keyword);
    StudentResponseDto getStudentByEmailNative(String email);
    List<StudentResponseDto> getAllStudentsSortedByFirstName();
    Page<StudentResponseDto> getStudent(Pageable pageable);
    List<StudentProjectionDTO> getStudentProjection();
    List<StudentResponseDto> searchStudents(String firstName,String email,String courses);
    List<StudentResponseDto> getAllStudentCustom();
    List<StudentResponseDto> getStudentByDepartmentCustom(String  departmentName);

}
