package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.service.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExamController.class)
public class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExamService examService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllExams() throws Exception {
        ExamDTO dto = new ExamDTO(1L, "Midterm Exam", null);
        when(examService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/exams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Midterm Exam"));
    }

    @Test
    void testGetExamById() throws Exception {
        ExamDTO dto = new ExamDTO(1L, "Final Exam", null);
        when(examService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/exams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Final Exam"));
    }

    @Test
    void testCreateExam() throws Exception {
        Exam exam = new Exam("New Exam", null);
        ExamDTO dto = new ExamDTO(1L, "New Exam", null);

        when(examService.save(any(Exam.class))).thenReturn(dto);

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Exam"));
    }

    @Test
    void testUpdateExam() throws Exception {
        Exam exam = new Exam("Updated Exam", null);
        ExamDTO dto = new ExamDTO(1L, "Updated Exam", null);

        when(examService.update(1L, exam)).thenReturn(dto);

        mockMvc.perform(put("/api/exams/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Exam"));
    }

    @Test
    void testDeleteExam() throws Exception {
        doNothing().when(examService).deleteById(1L);

        mockMvc.perform(delete("/api/exams/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
