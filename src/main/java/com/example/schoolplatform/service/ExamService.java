package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.ExamRepository;
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
        SubjectDTO subjectDTO = exam.getSubject() != null
                ? new SubjectDTO(exam.getSubject().getName())
                : null;

        return new ExamDTO(
                exam.getTitle(),
                subjectDTO
        );
    }
}
