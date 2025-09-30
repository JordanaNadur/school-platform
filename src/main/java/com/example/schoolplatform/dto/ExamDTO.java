package com.example.schoolplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ExamDTO(
        @NotBlank(message = "Exam title is required")
        @Size(min = 3, max = 100, message = "Exam title must be between 3 and 100 characters")
        String title,

        SubjectDTO subject
) {}
