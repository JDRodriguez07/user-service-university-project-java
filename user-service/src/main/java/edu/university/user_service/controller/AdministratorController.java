package edu.university.user_service.controller;

import edu.university.user_service.dto.AdministratorResponseDTO;
import edu.university.user_service.dto.CreateAdministratorDTO;
import edu.university.user_service.dto.UpdateAdministratorDTO;
import edu.university.user_service.service.AdministratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing administrators.
 */
@RestController
@RequestMapping("/administrators")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    @GetMapping
    public ResponseEntity<List<AdministratorResponseDTO>> getAll() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }

    @PostMapping
    public ResponseEntity<AdministratorResponseDTO> create(
            @Valid @RequestBody CreateAdministratorDTO dto) {
        return ResponseEntity.status(201)
                .body(administratorService.createAdministrator(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministratorResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdministratorDTO dto) {
        return ResponseEntity.ok(administratorService.updateAdministrator(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}
