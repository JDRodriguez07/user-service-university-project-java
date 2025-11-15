package edu.university.user_service.service;

import edu.university.user_service.enums.StudentStatus;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.EmailAlreadyExistsException;
import edu.university.user_service.exceptions.InvalidDniForCodeGenerationException;
import edu.university.user_service.exceptions.RoleNotFoundException;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.Student;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.StudentRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all students with pagination support.
     */
    public Page<Student> getAllStudents(@NonNull Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    /**
     * Find a student by id.
     */
    public Optional<Student> getStudentById(@NonNull Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Find a student by its student code.
     */
    public Optional<Student> getStudentByCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    /**
     * Get students by career.
     */
    public List<Student> getStudentsByCareer(String career) {
        return studentRepository.findByCareer(career);
    }

    /**
     * Get students by academic status (ENROLLED, GRADUATED, etc.).
     */
    public List<Student> getStudentsByStatus(StudentStatus status) {
        return studentRepository.findByStudentStatus(status);
    }

    /**
     * Get students by admission date range.
     */
    public List<Student> getStudentsByAdmissionBetween(LocalDate from, LocalDate to) {
        return studentRepository.findByAdmissionDateBetween(from, to);
    }

    /**
     * CREATE: creates a new Student ensuring:
     * - email is unique across users
     * - role is STUDENT
     * - user-status defaults to ACTIVE if null
     * - studentStatus defaults to ENROLLED if null
     * - studentCode is generated as S + last 6 digits of DNI
     */
    @Transactional
    public Student createStudent(Student student) {

        // Verificar email duplicado a nivel de usuarios
        if (userRepository.existsByEmail(student.getEmail())) {
            throw new EmailAlreadyExistsException(student.getEmail());
        }

        // Obtener el rol STUDENT
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RoleNotFoundException("STUDENT"));

        student.setRole(studentRole);

        if (student.getStatus() == null) {
            student.setStatus(UserStatus.ACTIVE);
        }

        if (student.getStudentStatus() == null) {
            student.setStudentStatus(StudentStatus.ENROLLED);
        }

        // Generar código automáticamente: S + últimos 6 dígitos del DNI
        String studentCode = generateCodeFromDni("S", student.getDni());
        student.setStudentCode(studentCode);

        return studentRepository.save(student);
    }

    /**
     * UPDATE parcial de un estudiante.
     */
    @Transactional
    public Optional<Student> updateStudent(Long id, Student partial) {
        return studentRepository.findById(id).map(db -> {

            // email heredado de User
            if (partial.getEmail() != null && !partial.getEmail().equals(db.getEmail())) {
                if (userRepository.existsByEmail(partial.getEmail())) {
                    throw new EmailAlreadyExistsException(partial.getEmail());
                }
                db.setEmail(partial.getEmail());
            }

            // Campos propios de Student
            if (partial.getStudentCode() != null) {
                db.setStudentCode(partial.getStudentCode());
            }
            if (partial.getAdmissionDate() != null) {
                db.setAdmissionDate(partial.getAdmissionDate());
            }
            if (partial.getGraduationDate() != null) {
                db.setGraduationDate(partial.getGraduationDate());
            }
            if (partial.getStudentStatus() != null) {
                db.setStudentStatus(partial.getStudentStatus());
            }
            if (partial.getGpa() != null) {
                db.setGpa(partial.getGpa());
            }
            if (partial.getCareer() != null) {
                db.setCareer(partial.getCareer());
            }

            // Datos personales heredados de Person (opcionales)
            if (partial.getName() != null)
                db.setName(partial.getName());
            if (partial.getLastName() != null)
                db.setLastName(partial.getLastName());
            if (partial.getPhoneNumber() != null)
                db.setPhoneNumber(partial.getPhoneNumber());
            if (partial.getAddress() != null)
                db.setAddress(partial.getAddress());

            return db;
        });
    }

    /**
     * DELETE: removes a student by id.
     */
    @Transactional
    public boolean deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    // =================== HELPERS ===================

    /**
     * Generates a code using a prefix and the last 6 digits of the given DNI.
     * Throws InvalidDniForCodeGenerationException if the DNI is null, blank
     * or does not contain any digit.
     */
    private String generateCodeFromDni(String prefix, String dni) {

        if (dni == null || dni.isBlank()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        // Opcional: dejar solo dígitos (por si DNI tiene puntos, guiones, etc.)
        String cleaned = dni.replaceAll("\\D", "");

        if (cleaned.isEmpty()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        String lastDigits;
        if (cleaned.length() <= 6) {
            lastDigits = cleaned;
        } else {
            lastDigits = cleaned.substring(cleaned.length() - 6);
        }

        return prefix + lastDigits;
    }
}
