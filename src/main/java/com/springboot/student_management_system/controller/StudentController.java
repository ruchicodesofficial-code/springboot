package com.springboot.student_management_system.controller;
import com.springboot.student_management_system.dto.StudentProjectionDTO;
import com.springboot.student_management_system.dto.StudentRequestDto;
import com.springboot.student_management_system.dto.StudentResponseDto;
import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.payload.ApiResponse;
import com.springboot.student_management_system.projection.StudentProjection;
import com.springboot.student_management_system.repository.StudentRepository;
import com.springboot.student_management_system.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //http://localhost:8080/api/hello
    // get all student
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents(){
        List<StudentResponseDto> students = studentService.getAllStudents();
        ApiResponse<List<StudentResponseDto>> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student fetched successfully",
                students
        );
        return ResponseEntity.ok(response);
    }
    //get student by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@PathVariable Long id){
        StudentResponseDto student = studentService.getStudentById(id);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student fetched successfully",
                student
        );
        return ResponseEntity.ok(response);
    }

    //save student
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDto>> saveStudent(@Valid
            @RequestBody StudentRequestDto dto)  {
        StudentResponseDto savedStudent = studentService.saveStudent(dto);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Student created successfully",
                savedStudent
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudent(@PathVariable Long id,
                                                            @Valid
                                                            @RequestBody StudentRequestDto dto){

        StudentResponseDto updateStudent = studentService.updateStudent(id,dto);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student updated successfully",
                updateStudent
        );
        return ResponseEntity.ok(response);

    }
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> patchStudent(@PathVariable Long id,@RequestBody  StudentRequestDto dto){
        StudentResponseDto updatedStudent = studentService.patchStudent(id,dto);
        ApiResponse<StudentResponseDto> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student updated successfully",
                updatedStudent
        );
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        ApiResponse<Object> response = new ApiResponse<>(
                true,
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Student deleted successfully",
                null
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<StudentResponseDto>getStudentByEmail(@PathVariable String email){
        return ResponseEntity.ok(
                studentService.getStudentByEmail(email)
        );
    }
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getStudentCountByCourse(@RequestParam String courseName){
        Long count =  studentService.getStudentCountByCourse(courseName);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Student count fetched successfully",
                        count
                )

        );
    }
    @DeleteMapping("/email/{email}")
    public ResponseEntity<ApiResponse<String >> deleteStudentByEmail(@PathVariable String email){
        studentService.deleteStudentByEmail(email);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        LocalDateTime.now(),
                        HttpStatus.OK.value(),
                        "Student deleted successfully",
                        null

                )
        );
    }
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDto>> getStudents(@RequestParam String firstName,
                                                                @RequestParam String course)
    {
        return ResponseEntity.ok(
                studentService.getStudentByFirstNameAndCourse(firstName,course)
    );
    }
    @GetMapping("/search/name")
    public ResponseEntity<List<StudentResponseDto>> getStudentByFirstName(@RequestParam String firstName)
    {
        return ResponseEntity.ok(
                studentService.findByFirstNameContaining(firstName)
        );
    }
    @GetMapping("/native/email/{email}")
    public ResponseEntity<StudentResponseDto> getStudentByEmailNative(@PathVariable String email){
        StudentResponseDto response = studentService.getStudentByEmailNative(email);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/sort")
    public ResponseEntity<List<StudentResponseDto>> getSortedStudents(){
        return ResponseEntity.ok(studentService.getAllStudentsSortedByFirstName());
    }
    @GetMapping("/page")
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(Pageable pageable){
        return ResponseEntity.ok(studentService.getStudent(pageable));
    }
//    @GetMapping("/projection")
//    public ResponseEntity<List<StudentProjection>> getStudentProjection(){
//        return ResponseEntity.ok(studentService.getStudentProjection());
//    }

    @GetMapping("/projection/dto")
    public List<StudentProjectionDTO> getStudentProjection(){
        return studentService.getStudentProjection();
    }
@GetMapping("/search/specification")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> searchStudents(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String courses
){
    List<StudentResponseDto> students = studentService.searchStudents(firstName,email,courses);
    ApiResponse<List<StudentResponseDto>> response = new ApiResponse<>(
            true,
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Student fetched successfully",
            students
    );
    return ResponseEntity.ok(response);
}
@GetMapping("/custom")
public List<StudentResponseDto> getAllStudentCustom(){
        return studentService.getAllStudentCustom();
}
@GetMapping("/custom/depart")
public List<StudentResponseDto> getStudentByDepartmentCustom(@RequestParam  String departmentName){
        return studentService.getStudentByDepartmentCustom(departmentName);
}
}

