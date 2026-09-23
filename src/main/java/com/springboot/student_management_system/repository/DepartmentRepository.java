package com.springboot.student_management_system.repository;

import com.springboot.student_management_system.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department,Long>
{

}
