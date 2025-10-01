package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<Page<StudentDTO>> getAllStudents(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "3") int size,
                                                           @RequestParam(defaultValue = "id") String sort,
                                                           @RequestParam(defaultValue = "asc") String direction){
        return ResponseEntity.ok(studentService.findAll(page, size, sort, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @PostMapping
    public StudentDTO createStudent(@Valid @RequestBody Student student) {
        return studentService.save(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody Student studentDetails) {
        return ResponseEntity.ok(studentService.update(id, studentDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
