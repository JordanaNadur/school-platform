package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.ExamRepository;
import com.example.schoolplatform.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubjectRepository subjectRepository;

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
        if (exam.getSubject() == null || exam.getSubject().getId() == null) {
            throw new ResourceNotFoundException("Subject must be provided for Exam");
        }

        Subject subject = subjectRepository.findById(exam.getSubject().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + exam.getSubject().getId()));

        exam.setSubject(subject);
        Exam savedExam = examRepository.save(exam);
        return toDTO(savedExam);
    }

    public ExamDTO update(Long id, Exam examDetails) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + id));

        exam.setTitle(examDetails.getTitle());

        if (examDetails.getSubject() != null && examDetails.getSubject().getId() != null) {
            Subject subject = subjectRepository.findById(examDetails.getSubject().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + examDetails.getSubject().getId()));
            exam.setSubject(subject);
        }

        Exam savedExam = examRepository.save(exam);
        return toDTO(savedExam);
    }

    public void deleteById(Long id) {
        if (!examRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exam not found with id " + id);
        }
        examRepository.deleteById(id);
    }

    private ExamDTO toDTO(Exam exam) {
        SubjectDTO subjectDTO = exam.getSubject() != null
                ? new SubjectDTO(exam.getSubject().getId(), exam.getSubject().getName())
                : null;

        return new ExamDTO(exam.getId(), exam.getTitle(), subjectDTO);
    }
}
