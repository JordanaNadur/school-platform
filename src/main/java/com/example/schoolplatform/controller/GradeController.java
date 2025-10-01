package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    public  ResponseEntity<Page<GradeDTO>> getAllGrades(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "6") int size,
                                                        @RequestParam(defaultValue = "id") String sort,
                                                        @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(gradeService.findAll(page, size, sort, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeDTO> getGradeById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.findById(id));
    }

    @PostMapping
    public GradeDTO createGrade(@Valid @RequestBody Grade grade) {
        return gradeService.save(grade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeDTO> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody Grade gradeDetails
    ) {
        return ResponseEntity.ok(gradeService.update(id, gradeDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
