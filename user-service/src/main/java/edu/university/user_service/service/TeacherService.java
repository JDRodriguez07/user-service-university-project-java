package edu.university.user_service.service;

import edu.university.user_service.dto.CreateTeacherDTO;
import edu.university.user_service.dto.TeacherResponseDTO;
import edu.university.user_service.dto.UpdateTeacherDTO;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.*;
import edu.university.user_service.mapper.TeacherMapper;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.Teacher;
import edu.university.user_service.repository.PersonRepository;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.TeacherRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(teacherMapper::toResponse)
                .toList();
    }

    public TeacherResponseDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
        return teacherMapper.toResponse(teacher);
    }

    @Transactional
    public TeacherResponseDTO createTeacher(CreateTeacherDTO dto) {

        // Validar email duplicado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        // Validar combinación documentType + dni
        if (personRepository.existsByDocumentTypeAndDni(dto.getDocumentType(), dto.getDni())) {
            throw new DocumentTypeAndDniAlreadyExistsException();
        }

        Teacher teacher = teacherMapper.toEntity(dto);

        // Encriptar password
        teacher.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleRepository.findByName("TEACHER")
                .orElseThrow(() -> new RoleNotFoundException("TEACHER"));
        teacher.setRole(role);

        if (teacher.getStatus() == null) {
            teacher.setStatus(UserStatus.ACTIVE);
        }

        String teacherCode = generateCodeFromDni("TEA", teacher.getDni());
        teacher.setTeacherCode(teacherCode);

        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toResponse(saved);
    }

    @Transactional
    public TeacherResponseDTO updateTeacher(Long id, UpdateTeacherDTO dto) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));

        if (dto.getEmail() != null && !dto.getEmail().equals(teacher.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
        }

        teacherMapper.updateEntityFromDto(dto, teacher);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            teacher.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Teacher updated = teacherRepository.save(teacher);
        return teacherMapper.toResponse(updated);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));

        // Soft delete
        teacher.setStatus(UserStatus.DELETED);
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
