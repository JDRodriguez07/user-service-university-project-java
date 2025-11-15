package edu.university.user_service.service;

import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.EmailAlreadyExistsException;
import edu.university.user_service.exceptions.InvalidDniForCodeGenerationException;
import edu.university.user_service.exceptions.RoleNotFoundException;
import edu.university.user_service.model.Administrator;
import edu.university.user_service.model.Role;
import edu.university.user_service.repository.AdministratorRepository;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdministratorService {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all administrators with pagination support.
     */
    public Page<Administrator> getAllAdministrators(@NonNull Pageable pageable) {
        return administratorRepository.findAll(pageable);
    }

    /**
     * Find an administrator by id.
     */
    public Optional<Administrator> getAdministratorById(@NonNull Long id) {
        return administratorRepository.findById(id);
    }

    /**
     * Find an administrator by its admin code.
     */
    public Optional<Administrator> getAdministratorByCode(String adminCode) {
        return administratorRepository.findByAdminCode(adminCode);
    }

    /**
     * Get administrators by department.
     */
    public List<Administrator> getAdministratorsByDepartment(String department) {
        return administratorRepository.findByDepartment(department);
    }

    /**
     * CREATE: creates a new Administrator ensuring:
     * - email is unique
     * - role is ADMIN
     * - user status defaults to ACTIVE if null
     */
    @Transactional
    public Administrator createAdministrator(Administrator admin) {

        // Verificar email duplicado a nivel de usuarios
        if (userRepository.existsByEmail(admin.getEmail())) {
            throw new EmailAlreadyExistsException(admin.getEmail());
        }

        // Obtener el rol ADMIN
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RoleNotFoundException("ADMIN"));

        admin.setRole(adminRole);

        if (admin.getStatus() == null) {
            admin.setStatus(UserStatus.ACTIVE);
        }

        // Generar código automáticamente: A + últimos 6 dígitos del DNI
        String adminCode = generateCodeFromDni("A", admin.getDni());
        admin.setAdminCode(adminCode);

        return administratorRepository.save(admin);
    }

    /**
     * UPDATE parcial de un administrador.
     * Se pueden actualizar datos específicos de Administrator
     * y, si quieres, también datos heredados de Person/User.
     */
    @Transactional
    public Optional<Administrator> updateAdministrator(Long id, Administrator partial) {
        return administratorRepository.findById(id).map(db -> {

            // email (heredado de User)
            if (partial.getEmail() != null && !partial.getEmail().equals(db.getEmail())) {
                if (userRepository.existsByEmail(partial.getEmail())) {
                    throw new EmailAlreadyExistsException(partial.getEmail());
                }
                db.setEmail(partial.getEmail());
            }

            // campos propios de Administrator
            if (partial.getAdminCode() != null) {
                db.setAdminCode(partial.getAdminCode());
            }

            if (partial.getDepartment() != null) {
                db.setDepartment(partial.getDepartment());
            }

            if (partial.getPosition() != null) {
                db.setPosition(partial.getPosition());
            }

            // campos heredados de Person (opcionales, si los mandas)
            if (partial.getName() != null) {
                db.setName(partial.getName());
            }

            if (partial.getLastName() != null) {
                db.setLastName(partial.getLastName());
            }

            if (partial.getPhoneNumber() != null) {
                db.setPhoneNumber(partial.getPhoneNumber());
            }

            if (partial.getAddress() != null) {
                db.setAddress(partial.getAddress());
            }

            return db;
        });
    }

    /**
     * DELETE: borra un administrador por id.
     * Retorna true si se eliminó, false si no existía.
     */
    @Transactional
    public boolean deleteAdministrator(Long id) {
        if (!administratorRepository.existsById(id)) {
            return false;
        }
        administratorRepository.deleteById(id);
        return true;
    }

    private String generateCodeFromDni(String prefix, String dni) {

        if (dni == null || dni.isBlank()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        // Opcional: limpiar caracteres no numéricos
        String cleaned = dni.replaceAll("\\D", "");

        if (cleaned.isEmpty()) {
            throw new InvalidDniForCodeGenerationException(dni);
        }

        // Tomar los últimos 6 dígitos (o menos si el DNI es más corto)
        String lastDigits;
        if (cleaned.length() <= 6) {
            lastDigits = cleaned;
        } else {
            lastDigits = cleaned.substring(cleaned.length() - 6);
        }

        return prefix + lastDigits;
    }

}
