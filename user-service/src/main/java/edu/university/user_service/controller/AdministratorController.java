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
    public ResponseEntity<List<AdministratorResponseDTO>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministratorResponseDTO> getAdministratorById(@PathVariable Long id) {
        return ResponseEntity.ok(administratorService.getAdministratorById(id));
    }

    @PostMapping
    public ResponseEntity<AdministratorResponseDTO> createAdministrator(
            @Valid @RequestBody CreateAdministratorDTO dto) {
        AdministratorResponseDTO created = administratorService.createAdministrator(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdministratorResponseDTO> updateAdministrator(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdministratorDTO dto) {
        AdministratorResponseDTO updated = administratorService.updateAdministrator(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }
}
