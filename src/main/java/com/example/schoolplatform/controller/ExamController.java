package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public List<ExamDTO> getAllExams() {
        return examService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDTO> getExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examService.findById(id));
    }

    @PostMapping
    public ExamDTO createExam(@Valid @RequestBody Exam exam) {
        return examService.save(exam);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamDTO> updateExam(
            @PathVariable Long id,
            @Valid @RequestBody Exam examDetails
    ) {
        return ResponseEntity.ok(examService.update(id, examDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
