package com.example.schoolplatform.service;

import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.repository.StudentRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.dto.GradeDTO;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentDTO> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public StudentDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return toDTO(student);
    }

    public StudentDTO save(Student student) {
        return toDTO(studentRepository.save(student));
    }

    public StudentDTO update(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        return toDTO(studentRepository.save(student));
    }

    public void deleteById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Student not found with id " + id);
        }
        studentRepository.deleteById(id);
    }

    private StudentDTO toDTO(Student student) {
        List<GradeDTO> gradeDTOs = student.getGrades() != null
                ? student.getGrades().stream()
                .map(g -> new GradeDTO(
                        g.getId(),
                        g.getValue(),
                        g.getStudent() != null ? g.getStudent().getId() : null,
                        g.getExam() != null ? g.getExam().getId() : null
                ))
                .toList()
                : List.of();

        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getEmail(),
                gradeDTOs
        );
    }

}
