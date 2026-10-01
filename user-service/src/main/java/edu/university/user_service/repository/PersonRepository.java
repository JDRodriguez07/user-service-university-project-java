package edu.university.user_service.repository;

import edu.university.user_service.enums.DocumentType;
import edu.university.user_service.model.Person;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {

    boolean existsByDocumentTypeAndDni(DocumentType documentType, String dni);

    Optional<Person> findByDni(String dni);

}
