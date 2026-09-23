package com.springboot.student_management_system.service;

import com.springboot.student_management_system.dto.*;
import com.springboot.student_management_system.entity.Address;
import com.springboot.student_management_system.entity.Course;
import com.springboot.student_management_system.entity.Department;
import com.springboot.student_management_system.entity.Student;
import com.springboot.student_management_system.exception.DuplicateEmailException;
import com.springboot.student_management_system.exception.ResourceNotFoundException;
import com.springboot.student_management_system.exception.StudentNotFoundException;
import com.springboot.student_management_system.projection.StudentProjection;
import com.springboot.student_management_system.repository.CourseRepository;
import com.springboot.student_management_system.repository.DepartmentRepository;
import com.springboot.student_management_system.repository.StudentRepository;
import com.springboot.student_management_system.specification.StudentSpecification;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService{
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StudentResponseDto saveStudent(StudentRequestDto dto)  {

        if (studentRepository.existsByEmail(dto.getEmail())){
            throw new DuplicateEmailException("Student already exists with email :"+dto.getEmail());
        }
        //Address entity
        Address address = new Address();
        address.setCity(dto.getAddress().getCity());
        address.setState(dto.getAddress().getState());
        address.setCountry(dto.getAddress().getCountry());

       //department entity
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + dto.getDepartmentId()));

        //Course entity
        List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
        //Student entity
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setPassword(
                passwordEncoder.encode(dto.getPassword()));
//        student.setCreatedAt(LocalDateTime.now());
        student.setDepartment(department);
        student.setCourses(courses);
        student.setAddress(address);
        Student savedStudent = studentRepository.save(student);
//        try{
//        sendConfirmationEmail();}
//        catch (Exception e){
//            log.error("Failed to send confirmation email ",e);
//        }
        return mapToResponseDto(savedStudent);
    }
private void sendConfirmationEmail(){
    throw new RuntimeException("Something went wrong!");
}

    @Override
    public List<StudentResponseDto > getAllStudents() {
        log.info("===================Fetching all students===================");
        List<StudentResponseDto> student = studentRepository.findAllWithDetails()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
        log.info("Successfully fetched {} student",student.size());
        return student;
    }

    @Override
    public StudentResponseDto  getStudentById(Long id) {
        log.debug("Fetching student with id: {}",id);
        Student student = studentRepository.findById(id)
                .orElseThrow(()-> {
                    log.warn("Student not found with id: {}", id);
                    return new StudentNotFoundException("Student not found with id: " + id);
                });
        log.info("Student fetched successfully with id: {}",id);
        return mapToResponseDto(student);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(()->
                new StudentNotFoundException("Student not found with id: "+id));
        student.setDeleted(true);
        studentRepository.save(student);
    }
    @Override
    public void deleteStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email).orElseThrow(()->
                new StudentNotFoundException("Student not found with email: "+email));
        student.setDeleted(true);
        studentRepository.save(student);
    }

     @Transactional
    @Override
    public StudentResponseDto  updateStudent(Long id, StudentRequestDto dto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(()->
                new StudentNotFoundException("Student not found with id: "+id));
        if(!existingStudent.getVersion().equals(dto.getVersion())){
            throw new OptimisticLockException("Student was already updated by another user");
        }
        //duplicate email check
        if (studentRepository.existsByEmail(dto.getEmail())&& !existingStudent.getEmail().equals(dto.getEmail())){
            throw new DuplicateEmailException("Student already exists with email: "+dto.getEmail());
        }
            existingStudent.setFirstName(dto.getFirstName());
            existingStudent.setLastName(dto.getLastName());
            existingStudent.setEmail(dto.getEmail());
            existingStudent.setPassword(passwordEncoder.encode(dto.getPassword()));

            if (dto.getAddress()!=null) {
                Address address = existingStudent.getAddress();
                if (address == null) {
                    address = new Address();
                }
                address.setCity(dto.getAddress().getCity());
                address.setState(dto.getAddress().getState());
                address.setCountry(dto.getAddress().getCountry());
                existingStudent.setAddress(address);
            }
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(()->
                        new ResourceNotFoundException("Department not found with id: "+dto.getDepartmentId()));
        existingStudent.setDepartment(department);
            Student updateStudent = studentRepository.save(existingStudent);

            List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
            existingStudent.setCourses(courses);
            return mapToResponseDto(updateStudent);
    }

    @Override
    public StudentResponseDto patchStudent(Long id, StudentRequestDto dto) {
        Student existingStudent = studentRepository.findById(id).orElseThrow(()->
            new StudentNotFoundException("Student not found with id: "+id));
            if (dto.getFirstName() != null) {
                existingStudent.setFirstName(dto.getFirstName());
            }
            if (dto.getLastName() != null) {
                existingStudent.setLastName(dto.getLastName());
            }
            if (dto.getEmail() != null) {
                if (studentRepository.existsByEmail(dto.getEmail())&& !existingStudent.getEmail().equals(dto.getEmail())){
                    throw new DuplicateEmailException("Student already exists with email: "+dto.getEmail());
                }
                existingStudent.setEmail(dto.getEmail());
            }
            if(dto.getPassword()!=null){
                existingStudent.setPassword(passwordEncoder.encode(dto.getPassword()));
            }
            if (dto.getAddress()!=null){
                Address address = getAddress(dto, existingStudent);

                existingStudent.setAddress(address);
            }
        if (dto.getDepartmentId()!=null) {
            Department department = departmentRepository.findById(dto.getDepartmentId()).orElseThrow(()->
                    new ResourceNotFoundException("Department not found with id: "+dto.getDepartmentId()));
            existingStudent.setDepartment(department);
        }

            Student updatedStudent = studentRepository.save(existingStudent);
            return mapToResponseDto(updatedStudent) ;
    }

    private static Address getAddress(StudentRequestDto dto, Student existingStudent) {
        Address address= existingStudent.getAddress();
        if (address ==null){
            address = new Address();

        }
        if (dto.getAddress().getCity()!=null){
            address.setCity(dto.getAddress().getCity());
        }
        if (dto.getAddress().getState()!=null){
            address.setState(dto.getAddress().getState());
        }
        if (dto.getAddress().getCountry()!=null){
            address.setCountry(dto.getAddress().getCountry());
        }
        return address;
    }

    @Override
    public StudentResponseDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(()->
                    new ResourceNotFoundException("Student not found with email: "+email)
                );
        return mapToResponseDto(student);
    }

    @Override
    public Long getStudentCountByCourse(String courseName) {

        return studentRepository.countStudentByCourse(courseName);
    }



    @Override
    public List<StudentResponseDto> getStudentByFirstNameAndCourse(String firstName, String course) {
        List<Student> students = studentRepository.findByFirstNameAndCourse(firstName,course);
        return students.stream()
                .map(this::mapToResponseDto).
                toList();
    }

    @Override
    public List<StudentResponseDto> findByFirstNameContaining(String keyword) {
        List<Student> students = studentRepository.findByFirstNameContaining(keyword);
        return students.stream()
                .map(this::mapToResponseDto).toList();
    }

    @Override
    public StudentResponseDto getStudentByEmailNative(String email) {
        Student student = studentRepository.findStudentByEmailNative(email)
                .orElseThrow(()->
                        new StudentNotFoundException("Student not found with email: "+email));

        return mapToResponseDto(student);
    }

    @Override
    public List<StudentResponseDto> getAllStudentsSortedByFirstName() {
//        Sort sort = Sort.by("firstName").ascending()
//                .and(Sort.by("lastName").descending());
//        List<Student> students = studentRepository.findAll(sort);
        List<Student> students = studentRepository.findAll(
                Sort.by("firstName").descending());
        return students.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public Page<StudentResponseDto> getStudent(Pageable pageable) {
        Page<Student> studentPage = studentRepository.findAll(pageable);
        return studentPage.map(this::mapToResponseDto);
    }

    @Override
    public List<StudentProjectionDTO> getStudentProjection() {
        return studentRepository.getStudentProjection();
    }

    @Override
    public List<StudentResponseDto> searchStudents(String firstName, String email, String courses) {
        Specification<Student> specification = Specification.allOf(
                StudentSpecification.hasFirstName(firstName),
               StudentSpecification.hasEmail(email),
               StudentSpecification.hasCourses(courses));
        List<Student> students = studentRepository.findAll(specification);
        return students.stream()
                .map(this::mapToResponseDto).toList();
    }

    @Override
    public List<StudentResponseDto> getAllStudentCustom() {
        List<Student> students = studentRepository.findStudentCustom();
        return students.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public List<StudentResponseDto> getStudentByDepartmentCustom(String departmentName) {
        List<Student> students = studentRepository.findStudentsByDepartment(departmentName);
        return students.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public StudentResponseDto uploadProfileImage(Long id, MultipartFile file) {
        Student student = studentRepository.findById(id)
                .orElseThrow(()->
                new StudentNotFoundException("Student not found with id: "+id));
        //validation
        if (file.isEmpty()){
            throw new IllegalArgumentException("File cannot be empty");
        }
        String contentType = file.getContentType();
        if (!"image/jpeg".equals(contentType)&& !"image/png".equals(contentType)){
            throw new IllegalArgumentException("only JPG and PNG images are allowed");
        }
        if (file.getSize()>5*1024*1024){
            throw new IllegalArgumentException("File size must be less than 5 MB");
        }
        try{
            String fileName = fileStorageService.storeFile(file);
            student.setProfileImage(fileName);
            Student updatedStudent= studentRepository.save(student);
            return mapToResponseDto(updatedStudent);
        }catch (IOException e){
            throw new RuntimeException("Failed to store profile image",e);
        }
    }

    @Override
    public Resource downloadProfileImage(Long id) {
       Student student = studentRepository.findById(id)
               .orElseThrow(()->
                       new StudentNotFoundException("Student not found with id: "+id));
       String fileName = student.getProfileImage();

       if (fileName==null||fileName.isBlank()){
           throw new ResourceNotFoundException("Profile image not found for student with id: "+id);

       }
       try {
           return fileStorageService.loadFileAsResource(fileName);
       }catch(IOException e){
           throw new ResourceNotFoundException("Profile image file not found: "+fileName);
       }
    }


    private StudentResponseDto mapToResponseDto(Student student){
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setProfileImage(student.getProfileImage());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        dto.setCreatedBy(student.getCreatedBy());
        dto.setLastModifiedBy(student.getLastModifiedBy());

        AddressResponseDTO addressDto = new AddressResponseDTO();
        addressDto.setCity(student.getAddress().getCity());
        addressDto.setState(student.getAddress().getState());
        addressDto.setCountry(student.getAddress().getCountry());
         dto.setAddress(addressDto);
        dto.setDepartmentName(student.getDepartment().getDepartmentName());
        dto.setCourses(
                student.getCourses()
                        .stream()
                        .map(course -> new
                                CourseResponseDTO(
                                        course.getId(),
                                course.getCourseName(),
                                course.getDuration(),
                                course.getFees(),
                                course.getInstructorName()
                        )).toList()
        );
        dto.setVersion(student.getVersion()) ;
        return dto;
    }
}
