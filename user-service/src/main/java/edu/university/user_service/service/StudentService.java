package edu.university.user_service.service;

import edu.university.user_service.dto.CreateStudentDTO;
import edu.university.user_service.dto.StudentResponseDTO;
import edu.university.user_service.dto.UpdateStudentDTO;
import edu.university.user_service.enums.StudentStatus;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.*;
import edu.university.user_service.mapper.StudentMapper;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.Student;
import edu.university.user_service.repository.PersonRepository;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.StudentRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository; // o RoleService si prefieres

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository; // para validar documentType + dni

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponse)
                .toList();
    }

    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return studentMapper.toResponse(student);
    }

    @Transactional
    public StudentResponseDTO createStudent(CreateStudentDTO dto) {

        // Validar email duplicado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        // Validar combinación documentType + dni
        if (personRepository.existsByDocumentTypeAndDni(dto.getDocumentType(), dto.getDni())) {
            throw new DocumentTypeAndDniAlreadyExistsException();
        }

        Student student = studentMapper.toEntity(dto);

        // Encriptar password
        student.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Rol STUDENT (puedes usar RoleService.getOrCreateRole si ya lo tienes)
        Role role = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RoleNotFoundException("STUDENT"));
        student.setRole(role);

        // Estado de usuario
        if (student.getStatus() == null) {
            student.setStatus(UserStatus.ACTIVE);
        }

        // Estado académico
        if (student.getStudentStatus() == null) {
            student.setStudentStatus(StudentStatus.ENROLLED);
        }

        // Fecha de admisión por defecto
        if (student.getAdmissionDate() == null) {
            student.setAdmissionDate(LocalDate.now());
        }

        // Generar código de estudiante: STU + últimos 6 dígitos del DNI
        String studentCode = generateCodeFromDni("STU", student.getDni());
        student.setStudentCode(studentCode);

        Student saved = studentRepository.save(student);
        return studentMapper.toResponse(saved);
    }

    @Transactional
    public StudentResponseDTO updateStudent(Long id, UpdateStudentDTO dto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        // Validar cambio de email
        if (dto.getEmail() != null && !dto.getEmail().equals(student.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
        }

        // Actualización parcial
        studentMapper.updateEntityFromDto(dto, student);

        // Si viene nueva contraseña, encriptarla
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            student.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Student updated = studentRepository.save(student);
        return studentMapper.toResponse(updated);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        // Soft delete
        student.setStatus(UserStatus.DELETED);
    }

    private String generateCodeFromDni(String prefix, String dni) {
        if (dni == null || dni.isBlank()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        String cleaned = dni.replaceAll("\\D", "");
        if (cleaned.isEmpty()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        String lastDigits = cleaned.length() < 6
                ? String.format("%06d", Integer.parseInt(cleaned))
                : cleaned.substring(cleaned.length() - 6);

        return prefix + lastDigits;
    }
}
