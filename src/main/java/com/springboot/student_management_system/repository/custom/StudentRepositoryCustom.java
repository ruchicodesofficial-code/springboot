package com.springboot.student_management_system.repository.custom;

import com.springboot.student_management_system.entity.Student;

import java.util.List;

public interface StudentRepositoryCustom {
    List<Student> findStudentCustom();
    List<Student> findStudentsByDepartment(String departmentName);
}
