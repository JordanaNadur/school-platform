package com.example.schoolplatform.controller;

import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public List<Grade> getAllGrades() {
        return gradeService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grade> getGradeById(@PathVariable Long id) {
        Grade grade = gradeService.findById(id);
        return ResponseEntity.ok(grade);
    }

    @PostMapping
    public Grade createGrade(@RequestBody Grade grade) {
        return gradeService.save(grade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grade> updateGrade(@PathVariable Long id, @RequestBody Grade gradeDetails) {
        Grade updatedGrade = gradeService.update(id, gradeDetails);
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
