package edu.university.user_service.service;

import edu.university.user_service.dto.AdministratorResponseDTO;
import edu.university.user_service.dto.CreateAdministratorDTO;
import edu.university.user_service.dto.UpdateAdministratorDTO;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.*;
import edu.university.user_service.mapper.AdministratorMapper;
import edu.university.user_service.model.Administrator;
import edu.university.user_service.model.Role;
import edu.university.user_service.repository.AdministratorRepository;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing administrators.
 */
@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdministratorMapper administratorMapper;

    public List<AdministratorResponseDTO> getAllAdministrators() {
        return administratorRepository.findAll()
                .stream()
                .map(administratorMapper::toResponse)
                .toList();
    }

    public AdministratorResponseDTO getAdministratorById(Long id) {
        Administrator admin = administratorRepository.findById(id)
                .orElseThrow(() -> new AdministratorNotFoundException(id));
        return administratorMapper.toResponse(admin);
    }

    @Transactional
    public AdministratorResponseDTO createAdministrator(CreateAdministratorDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        Administrator admin = administratorMapper.toEntity(dto);

        Role role = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RoleNotFoundException("ADMIN"));
        admin.setRole(role);

        // Código: A + últimos 6 dígitos del DNI
        String adminCode = generateCodeFromDni("ADM", admin.getDni());
        admin.setAdminCode(adminCode);

        Administrator saved = administratorRepository.save(admin);
        return administratorMapper.toResponse(saved);
    }

    @Transactional
    public AdministratorResponseDTO updateAdministrator(Long id, UpdateAdministratorDTO dto) {

        Administrator admin = administratorRepository.findById(id)
                .orElseThrow(() -> new AdministratorNotFoundException(id));

        if (dto.getEmail() != null && !dto.getEmail().equals(admin.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
        }

        administratorMapper.updateEntityFromDto(dto, admin);

        Administrator updated = administratorRepository.save(admin);
        return administratorMapper.toResponse(updated);
    }

    @Transactional
    public void deleteAdministrator(Long id) {
        if (!administratorRepository.existsById(id)) {
            throw new AdministratorNotFoundException(id);
        }
        administratorRepository.deleteById(id);
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
