package edu.university.user_service.service;

import edu.university.user_service.dto.CreateUserDTO;
import edu.university.user_service.dto.UpdateUserDTO;
import edu.university.user_service.dto.UserResponseDTO;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.EmailAlreadyExistsException;
import edu.university.user_service.exceptions.RoleNotFoundException;
import edu.university.user_service.exceptions.UserNotFoundException;
import edu.university.user_service.mapper.UserMapper;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.User;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for managing users.
 * Works with DTOs and delegates persistence to repositories.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    /**
     * Retrieves all users as DTOs.
     *
     * @return list of UserResponseDTO
     */
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the user ID
     * @return UserResponseDTO
     */
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return userMapper.toResponse(user);
    }

    /**
     * Creates a new user with a given role.
     *
     * @param dto data required to create the user
     * @return created user as UserResponseDTO
     */
    @Transactional
    public UserResponseDTO createUser(CreateUserDTO dto) {

        // Validar email duplicado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(dto.getEmail());
        }

        // Mapear DTO -> entidad User
        User user = userMapper.toEntity(dto);

        Role role = roleService.getOrCreateRole(dto.getRole(), "Generic user role");
        user.setRole(role);

        // Estado por defecto si no se ha configurado en otro lado
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        // Timestamps básicos (si no usas @PrePersist/@PreUpdate)
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // TODO: cuando integremos seguridad, encriptar password aquí (BCrypt)

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    /**
     * Updates an existing user. Only non-null fields in the DTO will be applied.
     *
     * @param id  the ID of the user to update
     * @param dto the update data
     * @return updated user as UserResponseDTO
     */
    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Validar cambio de email (si viene y es diferente)
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException(dto.getEmail());
            }
        }

        // Aplicar actualización parcial con MapStruct
        userMapper.updateEntityFromDto(dto, user);

        // Si viene un rol nuevo en el DTO, se actualiza
        if (dto.getRole() != null) {
            String roleName = dto.getRole().toUpperCase();
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RoleNotFoundException(roleName));
            user.setRole(role);
        }

        // Actualizar timestamp
        user.setUpdatedAt(LocalDateTime.now());

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    /**
     * Deletes a user by its ID.
     *
     * @param id the user ID
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setStatus(UserStatus.DELETED);
    }
}
