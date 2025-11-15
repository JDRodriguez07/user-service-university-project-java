package edu.university.user_service.service;

import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.EmailAlreadyExistsException;
import edu.university.user_service.exceptions.InvalidDniForCodeGenerationException;
import edu.university.user_service.exceptions.RoleNotFoundException;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.Teacher;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.TeacherRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all teachers with pagination support.
     */
    public Page<Teacher> getAllTeachers(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    /**
     * Find a teacher by id.
     */
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    /**
     * Find a teacher by its code.
     */
    public Optional<Teacher> getTeacherByCode(String teacherCode) {
        return teacherRepository.findByTeacherCode(teacherCode);
    }

    /**
     * Get teachers by academic degree.
     */
    public List<Teacher> getTeachersByDegree(edu.university.user_service.enums.AcademicDegree degree) {
        return teacherRepository.findByAcademicDegree(degree);
    }

    /**
     * Get teachers by contract type.
     */
    public List<Teacher> getTeachersByContractType(edu.university.user_service.enums.ContractType type) {
        return teacherRepository.findByContractType(type);
    }

    /**
     * CREATE: creates a new Teacher ensuring:
     * - email is unique
     * - role is TEACHER
     * - user status defaults to ACTIVE if null
     * - teacherCode is generated as T + last 6 digits of DNI
     */
    @Transactional
    public Teacher createTeacher(Teacher teacher) {

        // Verificar email duplicado a nivel de usuarios
        if (userRepository.existsByEmail(teacher.getEmail())) {
            throw new EmailAlreadyExistsException(teacher.getEmail());
        }

        // Obtener el rol TEACHER
        Role teacherRole = roleRepository.findByName("TEACHER")
                .orElseThrow(() -> new RoleNotFoundException("TEACHER"));

        teacher.setRole(teacherRole);

        if (teacher.getStatus() == null) {
            teacher.setStatus(UserStatus.ACTIVE);
        }

        // Generar código automáticamente: T + últimos 6 dígitos del DNI
        String teacherCode = generateCodeFromDni("T", teacher.getDni());
        teacher.setTeacherCode(teacherCode);

        return teacherRepository.save(teacher);
    }

    /**
     * UPDATE parcial de un teacher.
     */
    @Transactional
    public Optional<Teacher> updateTeacher(Long id, Teacher partial) {
        return teacherRepository.findById(id).map(db -> {

            // email heredado de User
            if (partial.getEmail() != null && !partial.getEmail().equals(db.getEmail())) {
                if (userRepository.existsByEmail(partial.getEmail())) {
                    throw new EmailAlreadyExistsException(partial.getEmail());
                }
                db.setEmail(partial.getEmail());
            }

            // Campos propios de Teacher
            if (partial.getTeacherCode() != null) {
                db.setTeacherCode(partial.getTeacherCode());
            }
            if (partial.getSpecialization() != null) {
                db.setSpecialization(partial.getSpecialization());
            }
            if (partial.getAcademicDegree() != null) {
                db.setAcademicDegree(partial.getAcademicDegree());
            }
            if (partial.getContractType() != null) {
                db.setContractType(partial.getContractType());
            }
            if (partial.getHireDate() != null) {
                db.setHireDate(partial.getHireDate());
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
     * DELETE: removes a teacher by id.
     */
    @Transactional
    public boolean deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            return false;
        }
        teacherRepository.deleteById(id);
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
            // mensaje: The provided DNI 'null/...' is invalid for code generation.
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
