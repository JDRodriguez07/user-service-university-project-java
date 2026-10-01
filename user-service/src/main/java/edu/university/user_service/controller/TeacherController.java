package edu.university.user_service.controller;

import edu.university.user_service.dto.CreateTeacherDTO;
import edu.university.user_service.dto.TeacherResponseDTO;
import edu.university.user_service.dto.UpdateTeacherDTO;
import edu.university.user_service.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAll() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> create(
            @Valid @RequestBody CreateTeacherDTO dto) {
        return ResponseEntity.status(201)
                .body(teacherService.createTeacher(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTeacherDTO dto) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
