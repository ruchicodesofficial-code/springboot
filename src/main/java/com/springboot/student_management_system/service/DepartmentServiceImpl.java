package com.springboot.student_management_system.service;

import com.springboot.student_management_system.dto.CourseResponseDTO;
import com.springboot.student_management_system.dto.DepartmentRequestDto;
import com.springboot.student_management_system.dto.DepartmentResponseDto;
import com.springboot.student_management_system.dto.StudentResponseDto;
import com.springboot.student_management_system.entity.Department;
import com.springboot.student_management_system.exception.ResourceNotFoundException;
import com.springboot.student_management_system.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl  implements DepartmentService{
    private final DepartmentRepository departmentRepository;


    @Override
    public DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto) {
        Department department = new Department();
        department.setDepartmentName(requestDto.getDepartmentName());
        Department savedDepartment = departmentRepository.save(department);
        return mapToResponseDto(savedDepartment);
    }

    @Override
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(this:: mapToResponseDto)
                .toList();
    }

    @Override
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Department not found with id: "+id));
        return mapToResponseDto(department);
    }
    private DepartmentResponseDto mapToResponseDto(Department department){
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setId(department.getId());
        dto.setDepartmentName(department.getDepartmentName());
        List<StudentResponseDto> students = department.getStudents()
                .stream()
                .map(student -> {
                        StudentResponseDto studentDto = new StudentResponseDto();
        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setEmail(student.getEmail());
        studentDto.setCourses(student.getCourses()
                .stream()
                .map(course->{
                    CourseResponseDTO courseDto = new CourseResponseDTO();
                    courseDto.setId(course.getId());
                    courseDto.setCourseName(course.getCourseName());
                    return courseDto;
                }).toList()
        );
        return studentDto;
                })
                .toList();
        dto.setStudents(students);
        return dto;
    }
}
