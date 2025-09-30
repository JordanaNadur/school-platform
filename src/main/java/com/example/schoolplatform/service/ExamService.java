package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.repository.ExamRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public List<ExamDTO> findAll() {
        return examRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ExamDTO findById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + id));
        return toDTO(exam);
    }

    public ExamDTO save(Exam exam) {
        return toDTO(examRepository.save(exam));
    }

    public ExamDTO update(Long id, Exam examDetails) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + id));

        exam.setTitle(examDetails.getTitle());
        exam.setSubject(examDetails.getSubject());

        return toDTO(examRepository.save(exam));
    }

    public void deleteById(Long id) {
        if (!examRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exam not found with id " + id);
        }
        examRepository.deleteById(id);
    }

    private ExamDTO toDTO(Exam exam) {
        List<GradeDTO> gradeDTOs = exam.getGrades() != null
                ? exam.getGrades().stream()
                .map(g -> new GradeDTO(
                        g.getId(),
                        g.getValue(),
                        g.getStudent() != null ? g.getStudent().getId() : null,
                        g.getExam() != null ? g.getExam().getId() : null
                ))
                .toList()
                : List.of();

        return new ExamDTO(
                exam.getId(),
                exam.getTitle(),
                exam.getSubject() != null ? exam.getSubject().getId() : null,
                gradeDTOs
        );
    }
}
