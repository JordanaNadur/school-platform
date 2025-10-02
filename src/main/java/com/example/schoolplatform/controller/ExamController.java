package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.service.ExamService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@Tag(name = "Exams", description = "Gerencia exames")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    @Operation(summary = "Lista todos os exames")
    public List<ExamDTO> getAllExams() {
        return examService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um exame pelo ID")
    public ResponseEntity<ExamDTO> getExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo exame")
    public ExamDTO createExam(@Valid @RequestBody Exam exam) {
        return examService.save(exam);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um exame existente")
    public ResponseEntity<ExamDTO> updateExam(@PathVariable Long id, @Valid @RequestBody Exam examDetails) {
        return ResponseEntity.ok(examService.update(id, examDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um exame pelo ID")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
