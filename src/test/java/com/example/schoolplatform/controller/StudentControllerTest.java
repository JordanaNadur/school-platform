package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        StudentDTO studentDTO = new StudentDTO(1L, "John Doe", "john@example.com", List.of());
        Page<StudentDTO> studentPage = new PageImpl<>(Arrays.asList(studentDTO));
        when(studentService.findAll(0, 3, "id", "asc")).thenReturn(studentPage);
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }

    @Test
    void testGetStudentById() throws Exception {
        StudentDTO studentDTO = new StudentDTO(1L, "John Doe", "john@example.com", List.of());
        when(studentService.findById(1L)).thenReturn(studentDTO);
        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testCreateStudent() throws Exception {
        StudentDTO studentDTO = new StudentDTO(null, "John Doe", "john@example.com", List.of());
        when(studentService.save(any(StudentDTO.class))).thenReturn(studentDTO);
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        StudentDTO studentDTO = new StudentDTO(null, "Jane Doe", "jane@example.com", List.of());
        when(studentService.update(1L, any(StudentDTO.class))).thenReturn(studentDTO);
        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}
