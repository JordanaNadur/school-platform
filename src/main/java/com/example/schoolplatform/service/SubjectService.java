package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.repository.SubjectRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public List<SubjectDTO> findAll() {
        return subjectRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public SubjectDTO findById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + id));
        return toDTO(subject);
    }

    public SubjectDTO save(Subject subject) {
        return toDTO(subjectRepository.save(subject));
    }

    public SubjectDTO update(Long id, Subject subjectDetails) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + id));

        subject.setName(subjectDetails.getName());

        return toDTO(subjectRepository.save(subject));
    }

    public void deleteById(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with id " + id);
        }
        subjectRepository.deleteById(id);
    }

    private SubjectDTO toDTO(Subject subject) {
        List<ExamDTO> examDTOs = subject.getExams() != null
                ? subject.getExams().stream()
                .map(e -> new ExamDTO(
                        e.getId(),
                        e.getTitle(),
                        e.getSubject() != null ? e.getSubject().getId() : null,
                        e.getGrades() != null
                                ? e.getGrades().stream()
                                .map(g -> new GradeDTO(
                                        g.getId(),
                                        g.getValue(),
                                        g.getStudent() != null ? g.getStudent().getId() : null,
                                        g.getExam() != null ? g.getExam().getId() : null
                                ))
                                .toList()
                                : List.of()
                ))
                .toList()
                : List.of();

        return new SubjectDTO(
                subject.getId(),
                subject.getName(),
                examDTOs
        );
    }

}
