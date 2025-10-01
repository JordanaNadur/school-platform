package com.example.schoolplatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectDTO(

        Long id,

        @NotBlank(message = "Subject name is required")
        @Size(min = 2, max = 50, message = "Subject name must be between 2 and 50 characters")
        String name
) {}
