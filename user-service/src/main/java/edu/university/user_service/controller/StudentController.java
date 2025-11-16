package edu.university.user_service.controller;

import edu.university.user_service.dto.CreateStudentDTO;
import edu.university.user_service.dto.StudentResponseDTO;
import edu.university.user_service.dto.UpdateStudentDTO;
import edu.university.user_service.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAll() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> create(
            @Valid @RequestBody CreateStudentDTO dto) {
        return ResponseEntity.status(201)
                .body(studentService.createStudent(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentDTO dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
