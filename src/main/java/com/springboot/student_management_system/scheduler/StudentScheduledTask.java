package com.springboot.student_management_system.scheduler;

import com.springboot.student_management_system.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StudentScheduledTask {
    private final StudentRepository studentRepository;

//      @Scheduled(fixedRate = 10000)
      //@Scheduled(fixedDelay = 10000)
//    @Scheduled(cron = "*/10 * * * * *")
//    public void printStudentCount(){
//        long studentCount = studentRepository.count();
//        log.info("Scheduled Task: Total students = {} ",studentCount);
//
//    }

}
