package edu.university.user_service.repository;

import edu.university.user_service.model.Administrator;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {

    Optional<Administrator> findByAdminCode(String adminCode);

    List<Administrator> findByDepartment(String department);

    Page<Administrator> findByPositionContainingIgnoreCase(String position, Pageable pageable);
}
