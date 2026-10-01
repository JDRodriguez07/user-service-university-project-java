package edu.university.user_service.service;

import edu.university.user_service.dto.CreateUserDTO;
import edu.university.user_service.dto.UpdateUserDTO;
import edu.university.user_service.dto.UpdateProfileDTO;
import edu.university.user_service.model.Person;
import edu.university.user_service.dto.UserResponseDTO;
import edu.university.user_service.dto.LoginRequestDTO;
import edu.university.user_service.dto.LoginResponseDTO;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.EmailAlreadyExistsException;
import edu.university.user_service.exceptions.RoleNotFoundException;
import edu.university.user_service.exceptions.UserNotFoundException;
import edu.university.user_service.mapper.UserMapper;
import edu.university.user_service.model.Role;
import edu.university.user_service.model.User;
import edu.university.user_service.repository.RoleRepository;
import edu.university.user_service.repository.UserRepository;
import edu.university.user_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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

        // Encriptar password
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = roleService.getOrCreateRole(dto.getRole(), "GENERIC");
        user.setRole(role);

        // Estado por defecto si no se ha configurado en otro lado
        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }

        // Timestamps básicos (si no dependes solo de @PrePersist/@PreUpdate)
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

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

        // Opcional: si permites cambiar password desde UpdateUserDTO
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Actualizar timestamp
        user.setUpdatedAt(LocalDateTime.now());

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     * Solo modifica: password, gender, phoneNumber y address.
     *
     * @param emailFromToken email extraído del token JWT (usuario autenticado)
     * @param dto            datos opcionales a actualizar
     */
    @Transactional
    public UserResponseDTO updateOwnProfile(String emailFromToken, UpdateProfileDTO dto) {

        User user = userRepository.findByEmail(emailFromToken)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + emailFromToken));

        // 1) Actualizar password si viene
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // 2) Actualizar datos de Person (solo si el usuario es Person:
        // Student/Teacher/Administrator)
        if (user instanceof Person person) {

            if (dto.getGender() != null) {
                person.setGender(dto.getGender());
            }

            if (dto.getPhoneNumber() != null) {
                person.setPhoneNumber(dto.getPhoneNumber());
            }

            if (dto.getAddress() != null) {
                person.setAddress(dto.getAddress());
            }
        }

        // 3) Timestamp de actualización
        user.setUpdatedAt(LocalDateTime.now());

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    /**
     * Soft delete a user by its ID (status = DELETED).
     *
     * @param id the user ID
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setStatus(UserStatus.DELETED);
    }

    /**
     * Authenticates a user with email and password and returns a JWT token.
     *
     * @param loginRequest login credentials (email, password)
     * @return LoginResponseDTO containing the JWT token
     * @throws AuthenticationException if credentials are invalid
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) throws AuthenticationException {

        // 1. Spring Security valida email y contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        // 2. Si la autenticación es exitosa, cargamos UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        // 3. Generamos el token
        String token = jwtService.generateToken(userDetails);

        // 4. Devolvemos el DTO de respuesta
        return new LoginResponseDTO(token);
    }
}
