package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Page<StudentDTO> findAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return studentRepository.findAll(pageable).map(
                this::toDTO
        );

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
                .map(g -> {
                    SubjectDTO subjectDTO = g.getExam() != null && g.getExam().getSubject() != null
                            ? new SubjectDTO(g.getExam().getSubject().getName())
                            : null;

                    ExamDTO examDTO = g.getExam() != null
                            ? new ExamDTO(g.getExam().getTitle(), subjectDTO)
                            : null;

                    return new GradeDTO(
                            g.getValue(),
                            student.getName(),
                            examDTO
                    );
                })
                .toList()
                : List.of();

        return new StudentDTO(
                student.getName(),
                student.getEmail(),
                gradeDTOs
        );
    }
}
