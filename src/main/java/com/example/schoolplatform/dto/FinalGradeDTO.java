package com.example.schoolplatform.dto;

import com.example.schoolplatform.entity.Grade;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FinalGradeDTO(
        @NotNull(message = "Final Grade value is required")
        @Min(value = 0, message = "Final Grade must be at least 0")
        @Max(value = 10, message = "Final Grade must be at most 10")
        Double value,

        @NotBlank(message = "Student name is required")
        String studentName,

        @NotBlank(message = "Subject name is required")
        SubjectDTO subject,

        List<Double>grades

) {}
