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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PersonRepository personRepository;

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

        // 1. Validar email único
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        // 1. Validar DNI único
        if (personRepository.existsByDocumentTypeAndDni(dto.getDocumentType(), dto.getDni())) {
            throw new PersonAlreadyExistsException(dto.getDocumentType().name(), dto.getDni());
        }

        // 2. Mapear DTO -> Entity
        Teacher teacher = teacherMapper.toEntity(dto);

        // 3. Asignar rol TEACHER
        Role role = roleService.getOrCreateRole("TEACHER", "Teacher role");
        teacher.setRole(role);

        // 4. Generar código de profesor: TEA + últimos 6 dígitos del DNI
        String teacherCode = generateCodeFromDni("TEA", teacher.getDni());
        teacher.setTeacherCode(teacherCode);

        // createdAt, updatedAt y status los maneja User con @PrePersist

        Teacher saved = teacherRepository.save(teacher);
        return teacherMapper.toResponse(saved);
    }

    @Transactional
    public TeacherResponseDTO updateTeacher(Long id, UpdateTeacherDTO dto) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));

        // Si cambia el email, validar que no exista en otro usuario
        if (dto.getEmail() != null && !dto.getEmail().equals(teacher.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
        }

        // Actualización parcial: solo campos no-null del DTO
        teacherMapper.updateEntityFromDto(dto, teacher);
        // status también lo actualiza el mapper si viene en el DTO
        // updatedAt lo actualiza User con @PreUpdate

        Teacher updated = teacherRepository.save(teacher);
        return teacherMapper.toResponse(updated);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));

        teacher.setStatus(UserStatus.DELETED);
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

        // Tomar los últimos 6 dígitos, padded si son menos
        String lastDigits = cleaned.length() < 6
                ? String.format("%06d", Integer.parseInt(cleaned))
                : cleaned.substring(cleaned.length() - 6);

        return prefix + lastDigits;
    }

}
