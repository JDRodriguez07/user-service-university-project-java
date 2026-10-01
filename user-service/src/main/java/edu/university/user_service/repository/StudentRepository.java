package edu.university.user_service.repository;

import edu.university.user_service.enums.StudentStatus;
import edu.university.user_service.model.Student;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentCode(String studentCode);

    List<Student> findByCareer(String career);

    List<Student> findByStudentStatus(StudentStatus status);

    Page<Student> findByCareerAndStudentStatus(String career, StudentStatus status, Pageable pageable);

    List<Student> findByAdmissionDateBetween(LocalDate from, LocalDate to);
}
