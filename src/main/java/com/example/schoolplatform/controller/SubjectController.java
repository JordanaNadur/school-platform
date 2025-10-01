package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.findById(id));
    }

    @PostMapping
    public SubjectDTO createSubject(@Valid @RequestBody Subject subject) {
        return subjectService.save(subject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody Subject subjectDetails
    ) {
        return ResponseEntity.ok(subjectService.update(id, subjectDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
