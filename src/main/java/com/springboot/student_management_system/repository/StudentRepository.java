package com.springboot.student_management_system.repository;

import com.springboot.student_management_system.dto.StudentProjectionDTO;
import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.projection.StudentProjection;
import com.springboot.student_management_system.repository.custom.StudentRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long>,
        JpaSpecificationExecutor<Student>, StudentRepositoryCustom {

    //----------- derived query-----------

    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);

    //-----------JPQL-----------

    @Query("""
            SELECT COUNT(s)
            FROM Student s
            JOIN s.courses c
            WHERE LOWER(c.courseName) = LOWER(:courseName)
            """)
    Long countStudentByCourse(@Param("courseName")String courseName);
    void deleteByEmail(String email);
    @Query("""
            SELECT s
            FROM Student s
            JOIN s.courses c
            WHERE s.firstName = :firstName
            AND LOWER(c.courseName) = LOWER(:course)
            """)
    List<Student> findByFirstNameAndCourse(String firstName, String course);
    List<Student> findByFirstNameContaining(String keyword);

    //--------------Native query-----------

    @Query(value ="SELECT * FROM students WHERE email=:email",
    nativeQuery=true)
    Optional<Student> findStudentByEmailNative(@Param("email")String email);
    List<StudentProjection> findBy();

    @Query("""
            SELECT new com.springboot.student_management_system.
            dto.StudentProjectionDTO(
            s.firstName,
            s.lastName,
            s.email
            )
            FROM Student s
            """)
    List<StudentProjectionDTO> getStudentProjection();
//    @Query("""
//            SELECT DISTINCT s
//            FROM Student s
//            LEFT JOIN FETCH s.courses
//            LEFT JOIN FETCH s.department
//            LEFT JOIN FETCH s.address
//            """)
//    List<Student> findAllWithDetails();
    @EntityGraph(attributePaths = {"address","department","courses"})
    @Query("SELECT s FROM Student s")
List<Student> findAllWithDetails();

}
