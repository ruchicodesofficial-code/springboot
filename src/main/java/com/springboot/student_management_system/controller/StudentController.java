package com.springboot.student_management_system.controller;

import com.springboot.student_management_system.dto.StudentProjectionDTO;
import com.springboot.student_management_system.dto.StudentRequestDto;
import com.springboot.student_management_system.dto.StudentResponseDto;
import com.springboot.student_management_system.payload.ApiResponse;
import com.springboot.student_management_system.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name= "Student Apis",
        description = "APIs for managing students"
)
public class StudentController {
private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    //http://localhost:8080/api/hello
    // get all student
    @Operation(
            summary = "Get all students",
            description = "Retrieves all active students"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Student retrieved successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "No Student found"
            )
    })
    @SecurityRequirement(name="bearerAuth")
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
    @Operation(
            summary = "Get Student by ID",
            description = "Retrieves a student using the student ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@Parameter(description = "Student ID",
    required = true) @PathVariable Long id){
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
    @Operation(summary = "Create a new student",
            description = "Creates a new student")
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

    //update student
    @Operation(summary = "Update student",
    description = "Updates an existing student using the student ID")
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

    //Patch student
    @Operation(summary = "Partially update student",
    description = "Updates selected fields of an existing student")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDto>> patchStudent(@PathVariable Long id,
                                                                        @RequestBody  StudentRequestDto dto){
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

    //delete
    @Operation(summary = "Delete student",
    description = "Deletes a student using the student ID")
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

    //get student by email
    @Operation(summary = "Get student by email",
    description = "Retrieves a student using the email address")
    @GetMapping("/email/{email}")
    public ResponseEntity<StudentResponseDto>getStudentByEmail(@PathVariable String email){
        return ResponseEntity.ok(
                studentService.getStudentByEmail(email)
        );
    }

    //Count students by course
    @Operation(summary = "Get student count by course",
    description = "Returns the number of students enrolled in a course")
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

    //Delete student by email
    @Operation(summary = "Delete student by email",
    description = "Deletes a student using the email address")
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

    //search student by first name and course
    @Operation(summary = "Search student",
    description = "Searches students by first name and course")
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDto>> getStudents(@RequestParam String firstName,
                                                                @RequestParam String course)
    {
        return ResponseEntity.ok(
                studentService.getStudentByFirstNameAndCourse(firstName,course)
    );
    }

    //search student by first name
    @Operation(summary = "Search student by first name",
    description = "Finds student whose first name contains the given value")
    @GetMapping("/search/name")
    public ResponseEntity<List<StudentResponseDto>> getStudentByFirstName(@RequestParam String firstName)
    {
        return ResponseEntity.ok(
                studentService.findByFirstNameContaining(firstName)
        );
    }

    //Native query - get student by email
    @Operation(summary = "Get student by email using native query",
    description = "Retrieves a student by email using a native SQL query")
    @GetMapping("/native/email/{email}")
    public ResponseEntity<StudentResponseDto> getStudentByEmailNative(@PathVariable String email){
        StudentResponseDto response = studentService.getStudentByEmailNative(email);
        return ResponseEntity.ok(response);
    }

    //sort students
    @Operation(summary = "Get sorted students",
    description = "Retrieves student sorted by first name")
    @GetMapping("/sort")
    public ResponseEntity<List<StudentResponseDto>> getSortedStudents(){
        return ResponseEntity.ok(studentService.getAllStudentsSortedByFirstName());
    }

    //pagination
    @Operation(summary = "Get students with pagination",
    description = "Retrieves students using pagination")
    @GetMapping("/page")
    public ResponseEntity<Page<StudentResponseDto>> getAllStudents(Pageable pageable){
        return ResponseEntity.ok(studentService.getStudent(pageable));
    }
//    @GetMapping("/projection")
//    public ResponseEntity<List<StudentProjection>> getStudentProjection(){
//        return ResponseEntity.ok(studentService.getStudentProjection());
//    }

    //projection DTO
    @Operation(summary = "Get students using projection",
    description = "Retrieves student data using DTO projection")
    @GetMapping("/projection/dto")
    public List<StudentProjectionDTO> getStudentProjection(){
        return studentService.getStudentProjection();
    }

    //specification search
    @Operation(summary = "Search student using specification",
    description = "Searches students dynamically using multiple optional filters")
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

//custom repository
    @Operation(summary = "Get all students using custom repository",
    description = "Retrieves students using custom repository implementation")
@GetMapping("/custom")
public List<StudentResponseDto> getAllStudentCustom(){
        return studentService.getAllStudentCustom();
}

//custom repository - department
    @Operation(summary = "Get students by department",
    description = "Retrieves students belonging to a department using custom repository")
@GetMapping("/custom/depart")
public List<StudentResponseDto> getStudentByDepartmentCustom(@RequestParam  String departmentName){
        return studentService.getStudentByDepartmentCustom(departmentName);
}

//@GetMapping("/header")
//public ResponseEntity<String> getClientVersion(
//        @RequestHeader("X-Client-Version") String version){
//        return ResponseEntity.ok("Client version:"+version);
//}
}

