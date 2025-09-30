package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.repository.GradeRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.schoolplatform.dto.GradeDTO;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public List<GradeDTO> findAll() {
        return gradeRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public GradeDTO findById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id " + id));
        return toDTO(grade);
    }

    public GradeDTO save(Grade grade) {
        return toDTO(gradeRepository.save(grade));
    }

    public GradeDTO update(Long id, Grade gradeDetails) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id " + id));

        grade.setValue(gradeDetails.getValue());
        grade.setStudent(gradeDetails.getStudent());
        grade.setExam(gradeDetails.getExam());

        return toDTO(gradeRepository.save(grade));
    }

    public void deleteById(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grade not found with id " + id);
        }
        gradeRepository.deleteById(id);
    }

    private GradeDTO toDTO(Grade grade) {
        SubjectDTO subjectDTO = grade.getExam() != null && grade.getExam().getSubject() != null
                ? new SubjectDTO(grade.getExam().getSubject().getName())
                : null;

        ExamDTO examDTO = grade.getExam() != null
                ? new ExamDTO(grade.getExam().getTitle(), subjectDTO)
                : null;

        String studentName = grade.getStudent() != null ? grade.getStudent().getName() : null;

        return new GradeDTO(
                grade.getValue(),
                studentName,
                examDTO
        );
    }
}

