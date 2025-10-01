package com.example.schoolplatform.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "grades")
@Data
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_value")
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    public Grade() {}

    public Grade(Double value, Student student, Exam exam) {
        this.value = value;
        this.student = student;
        this.exam = exam;
    }
}
