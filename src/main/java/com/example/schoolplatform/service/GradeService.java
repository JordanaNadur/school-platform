package com.example.schoolplatform.service;

import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.repository.GradeRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    public Grade findById(Long id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id " + id));
    }

    public Grade save(Grade grade) {
        return gradeRepository.save(grade);
    }

    public Grade update(Long id, Grade gradeDetails) {
        Grade grade = findById(id);
        grade.setValue(gradeDetails.getValue());
        grade.setStudent(gradeDetails.getStudent());
        grade.setExam(gradeDetails.getExam());
        return gradeRepository.save(grade);
    }

    public void deleteById(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grade not found with id " + id);
        }
        gradeRepository.deleteById(id);
    }
}
