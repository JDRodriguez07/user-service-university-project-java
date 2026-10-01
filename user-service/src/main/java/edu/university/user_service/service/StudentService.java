package edu.university.user_service.service;

import edu.university.user_service.dto.CreateStudentDTO;
import edu.university.user_service.dto.StudentResponseDTO;
import edu.university.user_service.dto.UpdateStudentDTO;
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

        if (student.getAdmissionDate() == null) {
            student.setAdmissionDate(LocalDate.now());
        }

        String studentCode = generateCodeFromDni("STU", student.getDni());
        student.setStudentCode(studentCode);

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

        // Actualización parcial de todos los campos (incluidos status y studentStatus)
        studentMapper.updateEntityFromDto(dto, student);

        Student updated = studentRepository.save(student);
        return studentMapper.toResponse(updated);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        student.setStatus(UserStatus.DELETED); // o INACTIVE
    }

    private String generateCodeFromDni(String prefix, String dni) {
        if (dni == null || dni.isBlank()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        // Mantener solo números
        String cleaned = dni.replaceAll("\\D", "");
        if (cleaned.isEmpty()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        // Tomar los últimos 6 dígitos (padded si vienen menos de 6)
        String lastDigits = cleaned.length() < 6
                ? String.format("%06d", Integer.parseInt(cleaned)) // agrega ceros a la izquierda
                : cleaned.substring(cleaned.length() - 6);

        return prefix + lastDigits;
    }

}
