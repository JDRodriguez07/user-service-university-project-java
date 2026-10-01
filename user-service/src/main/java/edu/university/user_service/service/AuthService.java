package edu.university.user_service.service;

import edu.university.user_service.dto.LoginRequestDTO;
import edu.university.user_service.dto.UserResponseDTO;
import edu.university.user_service.enums.UserStatus;
import edu.university.user_service.exceptions.InvalidCredentialsException;
import edu.university.user_service.exceptions.UserInactiveException;
import edu.university.user_service.mapper.UserMapper;
import edu.university.user_service.model.User;
import edu.university.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        // Bloquear login si el usuario no está activo
        if (user.getStatus() == UserStatus.DELETED
                || user.getStatus() == UserStatus.INACTIVE
                || user.getStatus() == UserStatus.BLOCKED) {
            throw new UserInactiveException(user.getStatus());
        }

        // Verificar contraseña
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return userMapper.toResponse(user);
    }
}
