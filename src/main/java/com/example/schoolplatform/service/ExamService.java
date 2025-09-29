package com.example.schoolplatform.service;

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

    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    public Exam findById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with id " + id));
    }

    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

    public Exam update(Long id, Exam examDetails) {
        Exam exam = findById(id);
        exam.setTitle(examDetails.getTitle());
        exam.setSubject(examDetails.getSubject());
        return examRepository.save(exam);
    }

    public void deleteById(Long id) {
        if (!examRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exam not found with id " + id);
        }
        examRepository.deleteById(id);
    }
}
