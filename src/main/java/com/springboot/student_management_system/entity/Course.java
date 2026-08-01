package com.springboot.student_management_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {//inverse side
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String courseName;
    private String duration;
    private Double fees;
    private String instructorName;
    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
}
