package edu.university.user_service.repository;

import edu.university.user_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // ADMIN, TEACHER, STUDENT
}
