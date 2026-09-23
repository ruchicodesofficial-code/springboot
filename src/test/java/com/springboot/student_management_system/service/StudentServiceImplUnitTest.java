package com.springboot.student_management_system.service;

import com.springboot.student_management_system.dto.StudentResponseDto;
import com.springboot.student_management_system.entity.Address;
import com.springboot.student_management_system.entity.Department;
import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.exception.StudentNotFoundException;
import com.springboot.student_management_system.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplUnitTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void shouldReturnStudentById(){
        //Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Usha");
        student.setLastName("yadav");
        student.setEmail("usha@gmail.com");

        Address address = new Address();
        address.setCity("udaipur");
        address.setState("Rajasthan");
        address.setCountry("India");
        student.setAddress(address);

        Department department = new Department();
        department.setDepartmentName("cs");
        student.setDepartment(department);
        student.setCourses(List.of());
        when(studentRepository.findById(anyLong()))
                .thenReturn(Optional.of(student));

        //Act
        StudentResponseDto result = studentService.getStudentById(10L);

        //Assert
        assertEquals(1L,result.getId());
        assertEquals("Usha",result.getFirstName());
        assertEquals("yadav",result.getLastName());
        assertEquals("usha@gmail.com",result.getEmail());
//        verify(studentRepository,times(1)).findById(1L);
//
//        verify(studentRepository,never()).deleteById(1L);
    }
@Test
    void shouldThrowExceptionWhenRepositoryFails(){
        //Arrange
        when(studentRepository.findById(1L))
                .thenThrow(new RuntimeException());

        //Act
    RuntimeException exception = assertThrows(
            RuntimeException.class,()->
                    studentService.getStudentById(1L)
    );

    }
    @Test
    void shouldThrowExceptionWhenStudentNotFound(){

        //Arrange
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());
        //Act & Assert
        assertThrows(
                StudentNotFoundException.class,()->
                        studentService.getStudentById(99L)
        );
    }
}
