package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.FinalGradeDTO;
import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllStudents() throws Exception {
        StudentDTO dto = new StudentDTO(1L, "John Doe", "john@example.com", List.of());
        when(studentService.findAll(0, 3, "id", "asc"))
                .thenReturn(new PageImpl<>(List.of(dto), PageRequest.of(0,3), 1));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }

    @Test
    void testGetStudentById() throws Exception {
        StudentDTO dto = new StudentDTO(1L, "John Doe", "john@example.com", List.of());
        when(studentService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetStudentFinalGrade() throws Exception {
        FinalGradeDTO finalGradeDTO = new FinalGradeDTO(
                "John Doe",
                new SubjectDTO(1L, "Math"),
                List.of(8.0, 9.0),
                8.5
        );
        when(studentService.getFinalGrade(1L, 1L)).thenReturn(finalGradeDTO);

        mockMvc.perform(get("/api/students/1/subject/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName").value("John Doe"))
                .andExpect(jsonPath("$.average").value(8.5));
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student("John Doe", "john@example.com");
        StudentDTO dto = new StudentDTO(1L, "John Doe", "john@example.com", List.of());

        when(studentService.save(any(Student.class))).thenReturn(dto);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student student = new Student("Jane Doe", "jane@example.com");
        StudentDTO dto = new StudentDTO(1L, "Jane Doe", "jane@example.com", List.of());

        when(studentService.update(1L, student)).thenReturn(dto);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk());
    }
}
