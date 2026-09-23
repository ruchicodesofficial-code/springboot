package com.springboot.student_management_system.specification;

import com.springboot.student_management_system.entity.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification
{
    public static Specification<Student> hasFirstName(String firstName){
        if (firstName==null||firstName.isBlank()){
            return null;
        }
return (root,query,criteriaBuilder)->
    criteriaBuilder.equal(
            root.get("firstName"),firstName);

    }
    public static Specification<Student> hasEmail(String email){
        if (email==null||email.isBlank()){
            return null;
        }
        return (root,query,criteriaBuilder)->
                criteriaBuilder.equal(root.get("email"),email);
    }
    public static Specification<Student> hasCourses(String courses){
        if (courses==null||courses.isBlank()){
            return null;
        }
        return (root,query,criteriaBuilder)->
                criteriaBuilder.equal(root.join("courses").get("courseName"),courses);
    }
}
