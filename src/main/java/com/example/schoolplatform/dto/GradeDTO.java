package com.example.schoolplatform.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GradeDTO(
        Long id,
        @NotNull(message = "Grade value is required")
        @Min(value = 0, message = "Grade must be at least 0")
        @Max(value = 10, message = "Grade must be at most 10")
        Double value,
        @NotBlank(message = "Student name is required")
        String studentName,
        ExamDTO exam
) {}
