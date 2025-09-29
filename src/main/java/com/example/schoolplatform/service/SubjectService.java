package com.example.schoolplatform.service;

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

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject findById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id " + id));
    }

    public Subject save(Subject subject) {
        return subjectRepository.save(subject);
    }

    public Subject update(Long id, Subject subjectDetails) {
        Subject subject = findById(id);
        subject.setName(subjectDetails.getName());
        return subjectRepository.save(subject);
    }

    public void deleteById(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Subject not found with id " + id);
        }
        subjectRepository.deleteById(id);
    }
}
