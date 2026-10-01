package edu.university.user_service.service;

import edu.university.user_service.dto.CreateTeacherDTO;
import edu.university.user_service.dto.TeacherResponseDTO;
import edu.university.user_service.dto.UpdateTeacherDTO;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.*;
import edu.university.user_service.mapper.TeacherMapper;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.Teacher;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.TeacherRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private TeacherMapper teacherMapper;

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

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Teacher teacher = teacherMapper.toEntity(dto);

        Role role = roleRepository.findByName("TEACHER")
                .orElseThrow(() -> new RoleNotFoundException("TEACHER"));
        teacher.setRole(role);

        if (teacher.getStatus() == null) {
            teacher.setStatus(UserStatus.ACTIVE);
        }

        String teacherCode = generateCodeFromDni("T", teacher.getDni());
        teacher.setTeacherCode(teacherCode);

        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

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

        if (dto.getStatus() != null) {
            teacher.setStatus(dto.getStatus());
        }

        teacher.setUpdatedAt(LocalDateTime.now());

        Teacher updated = teacherRepository.save(teacher);
        return teacherMapper.toResponse(updated);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new TeacherNotFoundException(id);
        }
        teacherRepository.deleteById(id);
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
