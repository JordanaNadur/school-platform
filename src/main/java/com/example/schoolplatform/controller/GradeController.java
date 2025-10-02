package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.service.GradeService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grades", description = "Gerencia notas de exames")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    @Operation(summary = "Lista todas as notas paginadas")
    public ResponseEntity<Page<GradeDTO>> getAllGrades(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size,
                                                       @RequestParam(defaultValue = "id") String sort,
                                                       @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(gradeService.findAll(page, size, sort, direction));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna uma nota pelo ID")
    public ResponseEntity<GradeDTO> getGradeById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova nota")
    public GradeDTO createGrade(@Valid @RequestBody Grade grade) {
        return gradeService.save(grade);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma nota existente")
    public ResponseEntity<GradeDTO> updateGrade(@PathVariable Long id, @Valid @RequestBody Grade gradeDetails) {
        return ResponseEntity.ok(gradeService.update(id, gradeDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma nota pelo ID")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
