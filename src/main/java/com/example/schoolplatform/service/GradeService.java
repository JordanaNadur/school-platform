package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public Page<GradeDTO> findAll(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return gradeRepository.findAll(pageable).map(
                this::toDTO
        );

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
