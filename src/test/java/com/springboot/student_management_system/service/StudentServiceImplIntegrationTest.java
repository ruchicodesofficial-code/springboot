package com.springboot.student_management_system.service;


import com.springboot.student_management_system.dto.StudentResponseDto;
import com.springboot.student_management_system.entity.Address;
import com.springboot.student_management_system.entity.Course;
import com.springboot.student_management_system.entity.Department;
import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.exception.StudentNotFoundException;
import com.springboot.student_management_system.repository.CourseRepository;
import com.springboot.student_management_system.repository.DepartmentRepository;
import com.springboot.student_management_system.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class StudentServiceImplIntegrationTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentService studentService;
    @Test
    void shouldReturnStudentById(){
        //Arrange
        Department department = new Department();
        department.setDepartmentName("computer science");
        department = departmentRepository.save(department);

        Course course  = new Course();
        course.setCourseName("Spring Boot");
        course.setDuration("6 Months");
        course.setFees(25000.0);
        course.setInstructorName("Ruchi");
        course = courseRepository.save(course);

        Address address = new Address();
        address.setCity("Indore");
        address.setState("MP");
        address.setCountry("India");

        Student student = new Student();
        student.setFirstName("Pooja");
        student.setLastName("Rao");
        student.setEmail("pooja@gmail.com");
        student.setPassword("Pooja#123");
        student.setAddress(address);
        student.setDepartment(department);
        student.setCourses(List.of(course));
        Student savedStudent = studentRepository.save(student);

        //Act
        StudentResponseDto result = studentService.getStudentById(savedStudent.getId());

        //Assert

        assertEquals(savedStudent.getId(),result.getId());
        assertEquals("Pooja",result.getFirstName());
        assertEquals("Rao",result.getLastName());
        assertEquals("pooja@gmail.com",result.getEmail());
        assertEquals("Indore",result.getAddress().getCity());
        assertEquals("MP",result.getAddress().getState());
        assertEquals("India",result.getAddress().getCountry());
        assertEquals("computer science",result.getDepartmentName());
        assertEquals(1,result.getCourses().size());
        assertEquals("Spring Boot",
                result.getCourses().get(0).getCourseName());

    }
    @Test
    void shouldThrowExceptionWhenStudentNotFound(){
        assertThrows(
                StudentNotFoundException.class,()->
            studentService.getStudentById(999999L)
        );
    }
}
