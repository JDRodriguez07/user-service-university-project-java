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
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.StudentRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentMapper studentMapper;

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

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Student student = studentMapper.toEntity(dto);

        Role role = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RoleNotFoundException("STUDENT"));
        student.setRole(role);

        if (student.getStatus() == null) {
            student.setStatus(UserStatus.ACTIVE);
        }

        if (student.getStudentStatus() == null) {
            student.setStudentStatus(StudentStatus.ENROLLED);
        }

        if (student.getAdmissionDate() == null) {
            student.setAdmissionDate(LocalDate.now());
        }

        String studentCode = generateCodeFromDni("S", student.getDni());
        student.setStudentCode(studentCode);

        LocalDateTime now = LocalDateTime.now();
        student.setCreatedAt(now);
        student.setUpdatedAt(now);

        Student saved = studentRepository.save(student);
        return studentMapper.toResponse(saved);
    }

    @Transactional
    public StudentResponseDTO updateStudent(Long id, UpdateStudentDTO dto) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        if (dto.getEmail() != null && !dto.getEmail().equals(student.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
        }

        studentMapper.updateEntityFromDto(dto, student);

        if (dto.getStatus() != null) {
            student.setStatus(dto.getStatus());
        }
        if (dto.getStudentStatus() != null) {
            student.setStudentStatus(dto.getStudentStatus());
        }

        student.setUpdatedAt(LocalDateTime.now());

        Student updated = studentRepository.save(student);
        return studentMapper.toResponse(updated);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    private String generateCodeFromDni(String prefix, String dni) {
        if (dni == null || dni.isBlank()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }
        String cleaned = dni.replaceAll("\\D", "");
        if (cleaned.isEmpty()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }
        String lastDigits = cleaned.length() <= 6
                ? cleaned
                : cleaned.substring(cleaned.length() - 6);
        return prefix + lastDigits;
    }
}
