package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.ExamRepository;
import com.example.schoolplatform.repository.GradeRepository;
import com.example.schoolplatform.repository.StudentRepository;
import com.example.schoolplatform.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public Page<GradeDTO> findAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return gradeRepository.findAll(pageable).map(this::toDTO);
    }

    public GradeDTO findById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id " + id));
        return toDTO(grade);
    }

    public GradeDTO save(Grade grade) {
        Student student = fetchStudent(grade);
        Exam exam = fetchExamWithSubject(grade);

        grade.setStudent(student);
        grade.setExam(exam);

        Grade savedGrade = gradeRepository.save(grade);
        return toDTO(savedGrade);
    }

    public GradeDTO update(Long id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id " + id));

        grade.setValue(gradeDetails.getValue());

        if (gradeDetails.getStudent() != null && gradeDetails.getStudent().getId() != null) {
            Student student = studentRepository.findById(gradeDetails.getStudent().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + gradeDetails.getStudent().getId()));
            grade.setStudent(student);
        }

        if (gradeDetails.getExam() != null && gradeDetails.getExam().getId() != null) {
            Exam exam = fetchExamWithSubject(gradeDetails);
            grade.setExam(exam);
        }

        Grade savedGrade = gradeRepository.save(grade);
        return toDTO(savedGrade);
    }

    public void deleteById(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grade not found with id " + id);
        }
        gradeRepository.deleteById(id);
    }

    private Student fetchStudent(Grade grade) {
        if (grade.getStudent() == null || grade.getStudent().getId() == null) {
            throw new ResourceNotFoundException("Student must be provided for Grade");
        }
        return studentRepository.findById(grade.getStudent().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + grade.getStudent().getId()));
    }

    private Exam fetchExamWithSubject(Grade grade) {
        if (grade.getExam() == null || grade.getExam().getId() == null) {
            throw new ResourceNotFoundException("Exam must be provided for Grade");
        }
        Exam exam = examRepository.findById(grade.getExam().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + grade.getExam().getId()));

        if (exam.getSubject() == null || exam.getSubject().getId() == null) {
            throw new ResourceNotFoundException("Subject must be provided for Exam");
        }
        Subject subject = subjectRepository.findById(exam.getSubject().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + exam.getSubject().getId()));

        exam.setSubject(subject);
        return exam;
    }

    private GradeDTO toDTO(Grade grade) {
        ExamDTO examDTO = grade.getExam() != null ? new ExamDTO(
                grade.getExam().getId(),
                grade.getExam().getTitle(),
                grade.getExam().getSubject() != null
                        ? new SubjectDTO(
                        grade.getExam().getSubject().getId(),
                        grade.getExam().getSubject().getName())
                        : null
        ) : null;

        String studentName = grade.getStudent() != null ? grade.getStudent().getName() : null;

        return new GradeDTO(grade.getId(), grade.getValue(), studentName, examDTO);
    }
}
