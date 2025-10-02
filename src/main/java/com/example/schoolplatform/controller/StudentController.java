package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.FinalGradeDTO;
import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.service.StudentService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Gerencia estudantes")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    @Operation(summary = "Lista todos os estudantes paginados")
    public ResponseEntity<Page<StudentDTO>> getAllStudents(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "3") int size,
                                                           @RequestParam(defaultValue = "id") String sort,
                                                           @RequestParam(defaultValue = "asc") String direction){
        return ResponseEntity.ok(studentService.findAll(page, size, sort, direction));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retorna um estudante pelo ID")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @GetMapping("/{id}/subject/{idSubject}")
    @Operation(summary = "Retorna a nota final do estudante para uma matéria específica")
    public ResponseEntity<FinalGradeDTO> getStudentFinalGrade(@PathVariable Long id, @PathVariable Long idSubject) {
        return ResponseEntity.ok(studentService.getFinalGrade(id, idSubject));
    }

    @PostMapping
    @Operation(summary = "Cria um novo estudante")
    public StudentDTO createStudent(@Valid @RequestBody Student student) {
        return studentService.save(student);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um estudante existente")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        return ResponseEntity.ok(studentService.update(id, studentDetails));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um estudante pelo ID")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
