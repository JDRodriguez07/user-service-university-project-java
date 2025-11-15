package edu.university.user_service.service;

import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.EmailAlreadyExistsException;
import edu.university.user_service.exceptions.RoleNotFoundException;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.User;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /** Para autenticación: trae User + Role en una sola consulta */
    public Optional<User> getUserByEmailWithRole(String email) {
        return userRepository.findByEmailWithRole(email);
    }

    /** CREATE: crea usuario con rol por nombre (ADMIN/TEACHER/STUDENT) */
    @Transactional
    public User createUser(User user, String roleName) {

        // Email duplicado → excepción personalizada
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        // Buscar rol por nombre → si no existe, excepción personalizada
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        user.setRole(role);

        if (user.getStatus() == null)
            user.setStatus(UserStatus.ACTIVE);

        // TODO: en seguridad, encriptar password con BCrypt

        return userRepository.save(user);
    }

    /** UPDATE parcial (email/password/status) */
    @Transactional
    public Optional<User> updateUser(Long id, User partial) {
        return userRepository.findById(id).map(db -> {

            // Validar nuevo email
            if (partial.getEmail() != null && !partial.getEmail().equals(db.getEmail())) {

                if (userRepository.existsByEmail(partial.getEmail())) {
                    throw new EmailAlreadyExistsException(partial.getEmail());
                }

                db.setEmail(partial.getEmail());
            }

            if (partial.getPassword() != null) {
                db.setPassword(partial.getPassword()); // TODO: codificar
            }

            if (partial.getStatus() != null) {
                db.setStatus(partial.getStatus());
            }

            return db; // entidad managed
        });
    }

    /** Cambiar estado de la cuenta */
    @Transactional
    public Optional<User> changeStatus(Long id, UserStatus status) {
        return userRepository.findById(id).map(db -> {
            db.setStatus(status);
            return db;
        });
    }

    /** Reasignar rol por nombre */
    @Transactional
    public Optional<User> assignRole(Long userId, String roleName) {
        return userRepository.findById(userId).map(db -> {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException(roleName));
            db.setRole(role);
            return db;
        });
    }

    /** DELETE */
    @Transactional
    public boolean deleteUser(Long id) {
        if (!userRepository.existsById(id))
            return false;
        userRepository.deleteById(id);
        return true;
    }
}
