package com.example.schoolplatform.controller;

import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.service.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
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
        Subject subject = new Subject("Mathematics");
        Exam exam = new Exam("Final Exam", subject);
        List<Exam> exams = Arrays.asList(exam);
        when(examService.findAll()).thenReturn(exams);
        mockMvc.perform(get("/api/exams"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Final Exam"));
    }

    @Test
    void testGetExamById() throws Exception {
        Subject subject = new Subject("Mathematics");
        Exam exam = new Exam("Final Exam", subject);
        when(examService.findById(1L)).thenReturn(exam);
        mockMvc.perform(get("/api/exams/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Final Exam"));
    }

    @Test
    void testCreateExam() throws Exception {
        Subject subject = new Subject("Mathematics");
        Exam exam = new Exam("Final Exam", subject);
        when(examService.save(any(Exam.class))).thenReturn(exam);
        mockMvc.perform(post("/api/exams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Final Exam"));
    }

    @Test
    void testUpdateExam() throws Exception {
        Subject subject = new Subject("Physics");
        Exam exam = new Exam("Midterm Exam", subject);
        when(examService.update(1L, any(Exam.class))).thenReturn(exam);
        mockMvc.perform(put("/api/exams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Midterm Exam"));
    }

    @Test
    void testDeleteExam() throws Exception {
        mockMvc.perform(delete("/api/exams/1"))
                .andExpect(status().isNoContent());
    }
}
