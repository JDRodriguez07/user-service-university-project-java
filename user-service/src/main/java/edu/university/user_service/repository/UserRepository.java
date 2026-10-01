package edu.university.user_service.repository;

import edu.university.user_service.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    long countByRole_Name(String name); // "ADMIN","TEACHER","STUDENT"

    /** Auth use-case: load User + Role in one shot */
    @Query("select u from User u join fetch u.role where u.email = :email")
    Optional<User> findByEmailWithRole(@Param("email") String email);
}
