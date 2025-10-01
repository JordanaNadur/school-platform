package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.FinalGradeDTO;
import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.GradeRepository;
import com.example.schoolplatform.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SubjectService subjectService;

    public Page<StudentDTO> findAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return studentRepository.findAll(pageable).map(this::toDTO);
    }

    public StudentDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
        return toDTO(student);
    }

    public StudentDTO save(Student student) {
        if (student.getGrades() != null && !student.getGrades().isEmpty()) {
            List<Grade> persistedGrades = student.getGrades().stream()
                    .map(g -> gradeRepository.findById(g.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Grade not found with id " + g.getId()
                            )))
                    .toList();
            student.setGrades(persistedGrades);
        }

        Student savedStudent = studentRepository.save(student);
        return toDTO(savedStudent);
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
                ? student.getGrades().stream().map(g -> new GradeDTO(
                g.getId(),
                g.getValue(),
                student.getName(),
                g.getExam() != null
                        ? new com.example.schoolplatform.dto.ExamDTO(
                        g.getExam().getId(),
                        g.getExam().getTitle(),
                        g.getExam().getSubject() != null
                                ? new com.example.schoolplatform.dto.SubjectDTO(
                                g.getExam().getSubject().getId(),
                                g.getExam().getSubject().getName())
                                : null)
                        : null
        )).toList()
                : List.of();

        return new StudentDTO(student.getId(), student.getName(), student.getEmail(), gradeDTOs);
    }

    public FinalGradeDTO getFinalGrade(Long idStudent, Long idSubject){
        Student student = studentRepository.findById(idStudent)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + idStudent));
        return toFinalGradeDTO(student, idSubject);

    }

    private FinalGradeDTO toFinalGradeDTO(Student student, Long idSubject) {
        SubjectDTO subject = subjectService.findById(idSubject);
        List<Grade> grades = student.getGrades();
        List<Double> gradeValues = new ArrayList<>();

        for (Grade grade : grades) {
            if (Objects.equals(grade.getExam().getSubject().getId(), idSubject)) {
                gradeValues.add(grade.getValue());
            }
        }

        Double finalGrade = gradeValues.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        return new FinalGradeDTO(
                student.getName(),
                subject,
                gradeValues,
                finalGrade
        );
    }

}
