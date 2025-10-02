package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.service.SubjectService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@Tag(name = "Subjects", description = "Gerencia matérias")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    @Operation(summary = "Lista todas as matérias")
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna uma matéria pelo ID")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova matéria")
    public SubjectDTO createSubject(@Valid @RequestBody Subject subject) {
        return subjectService.save(subject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma matéria existente")
    public ResponseEntity<SubjectDTO> updateSubject(@PathVariable Long id, @Valid @RequestBody Subject subjectDetails) {
        return ResponseEntity.ok(subjectService.update(id, subjectDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma matéria pelo ID")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
