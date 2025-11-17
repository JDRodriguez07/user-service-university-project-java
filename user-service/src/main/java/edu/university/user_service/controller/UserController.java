package edu.university.user_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import edu.university.user_service.dto.UpdateProfileDTO;
import edu.university.user_service.dto.CreateUserDTO;
import edu.university.user_service.dto.UpdateUserDTO;
import edu.university.user_service.dto.UserResponseDTO;
import edu.university.user_service.service.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        UserResponseDTO created = userService.createUser(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     * Solo permite cambiar password, gender, phoneNumber y address.
     *
     * URL: PUT /users/me/profile
     */
    @PutMapping("/me/profile")
    public ResponseEntity<UserResponseDTO> updateOwnProfile(
            Authentication authentication,
            @RequestBody UpdateProfileDTO dto) {

        // El email viene del UserDetails cargado a partir del JWT
        String email = authentication.getName();

        UserResponseDTO updated = userService.updateOwnProfile(email, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
