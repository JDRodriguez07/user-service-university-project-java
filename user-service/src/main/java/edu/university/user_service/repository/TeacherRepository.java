package edu.university.user_service.repository;

import edu.university.user_service.enums.AcademicDegree;
import edu.university.user_service.enums.ContractType;
import edu.university.user_service.model.Teacher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByTeacherCode(String teacherCode);

    List<Teacher> findByAcademicDegree(AcademicDegree degree);

    List<Teacher> findByContractType(ContractType type);

    Page<Teacher> findBySpecializationContainingIgnoreCase(String specialization, Pageable pageable);
}
