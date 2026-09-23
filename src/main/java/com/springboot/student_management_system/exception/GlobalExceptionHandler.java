package com.springboot.student_management_system.exception;

import com.springboot.student_management_system.payload.ApiResponse;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(StudentNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleStudentNotFoundException(StudentNotFoundException
                                                                      ex, HttpServletRequest request){
      log.warn("Student not found: {}",ex.getMessage());
    ApiResponse <Object> response = new ApiResponse<>(
            false,
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
           null
    );

    return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
          ResourceNotFoundException ex,HttpServletRequest request){
      log.warn("Resource not found: {}",ex.getMessage());
      ApiResponse<Object> response = new ApiResponse<>(
              false,
              LocalDateTime.now(),
              HttpStatus.NOT_FOUND.value(),
              ex.getMessage(),
              null
              );
      return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ApiResponse<Object>> handleDuplicateEmailException(DuplicateEmailException ex,HttpServletRequest request){
    ApiResponse<Object> response = new ApiResponse<>(
            false,
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
           null
    );
    return new ResponseEntity<>(response,HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest request){
    Map<String,String > errors = new HashMap<>();
    ex.getBindingResult()
            .getFieldErrors()
            .forEach(fieldError -> {
              errors.put(
                      fieldError.getField(),
                      fieldError.getDefaultMessage()
              );
            });
    ApiResponse<Object> response= new ApiResponse<>(
            false,
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
           "Validation Failed",
            errors
    );
    return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(OptimisticLockException.class)
  public ResponseEntity<ApiResponse<Object>> handleOptimisticLockException(OptimisticLockException ex, HttpServletRequest request){
      ApiResponse<Object> response = new ApiResponse<>(
              false,
              LocalDateTime.now(),
              HttpStatus.CONFLICT.value(),
              "Student was already updated by another user",
              null

      );
      return new ResponseEntity<>(response,HttpStatus.CONFLICT);
  }
}
