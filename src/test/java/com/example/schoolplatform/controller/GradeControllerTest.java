package com.example.schoolplatform.controller;

import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.service.GradeService;
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

@WebMvcTest(GradeController.class)
public class GradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GradeService gradeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllGrades() throws Exception {
        Student student = new Student("John Doe", "john@example.com");
        Subject subject = new Subject("Mathematics");
        Exam exam = new Exam("Final Exam", subject);
        Grade grade = new Grade(9.5, student, exam);
        List<Grade> grades = Arrays.asList(grade);
        when(gradeService.findAll()).thenReturn(grades);
        mockMvc.perform(get("/api/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").value(9.5));
    }

    @Test
    void testGetGradeById() throws Exception {
        Student student = new Student("John Doe", "john@example.com");
        Subject subject = new Subject("Mathematics");
        Exam exam = new Exam("Final Exam", subject);
        Grade grade = new Grade(9.5, student, exam);
        when(gradeService.findById(1L)).thenReturn(grade);
        mockMvc.perform(get("/api/grades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(9.5));
    }

    @Test
    void testCreateGrade() throws Exception {
        Student student = new Student("John Doe", "john@example.com");
        Subject subject = new Subject("Mathematics");
        Exam exam = new Exam("Final Exam", subject);
        Grade grade = new Grade(9.5, student, exam);
        when(gradeService.save(any(Grade.class))).thenReturn(grade);
        mockMvc.perform(post("/api/grades")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(9.5));
    }

    @Test
    void testUpdateGrade() throws Exception {
        Student student = new Student("Jane Doe", "jane@example.com");
        Subject subject = new Subject("Physics");
        Exam exam = new Exam("Midterm", subject);
        Grade grade = new Grade(8.0, student, exam);
        when(gradeService.update(1L, any(Grade.class))).thenReturn(grade);
        mockMvc.perform(put("/api/grades/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(grade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(8.0));
    }

    @Test
    void testDeleteGrade() throws Exception {
        mockMvc.perform(delete("/api/grades/1"))
                .andExpect(status().isNoContent());
    }
}
