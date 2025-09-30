package com.example.schoolplatform.dto;

public record GradeDTO(
        Double value,
        String studentName,
        ExamDTO exam
) {}
