package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.service.GradeService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        SubjectDTO subjectDTO = new SubjectDTO(1L, "Math");
        ExamDTO examDTO = new ExamDTO(1L, "Math Exam", subjectDTO);
        GradeDTO gradeDTO = new GradeDTO(1L, 9.0, "John Doe", examDTO);

        when(gradeService.findAll(0, 6, "id", "asc"))
                .thenReturn(new PageImpl<>(List.of(gradeDTO), PageRequest.of(0, 6), 1));

        mockMvc.perform(get("/api/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].studentName").value("John Doe"))
                .andExpect(jsonPath("$.content[0].value").value(9.0))
                .andExpect(jsonPath("$.content[0].exam.title").value("Math Exam"))
                .andExpect(jsonPath("$.content[0].exam.subject.name").value("Math"));
    }

    @Test
    void testGetGradeById() throws Exception {
        SubjectDTO subjectDTO = new SubjectDTO(2L, "History");
        ExamDTO examDTO = new ExamDTO(2L, "History Exam", subjectDTO);
        GradeDTO gradeDTO = new GradeDTO(2L, 8.5, "Jane Doe", examDTO);

        when(gradeService.findById(2L)).thenReturn(gradeDTO);

        mockMvc.perform(get("/api/grades/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName").value("Jane Doe"))
                .andExpect(jsonPath("$.value").value(8.5))
                .andExpect(jsonPath("$.exam.title").value("History Exam"))
                .andExpect(jsonPath("$.exam.subject.name").value("History"));
    }

    @Test
    void testCreateGrade() throws Exception {
        Grade grade = new Grade();
        grade.setValue(10.0);

        SubjectDTO subjectDTO = new SubjectDTO(3L, "Science");
        ExamDTO examDTO = new ExamDTO(3L, "Science Exam", subjectDTO);
        GradeDTO dto = new GradeDTO(3L, 10.0, "John Doe", examDTO);

        when(gradeService.save(any(Grade.class))).thenReturn(dto);

        mockMvc.perform(post("/api/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(10.0))
                .andExpect(jsonPath("$.studentName").value("John Doe"))
                .andExpect(jsonPath("$.exam.title").value("Science Exam"))
                .andExpect(jsonPath("$.exam.subject.name").value("Science"));
    }

    @Test
    void testUpdateGrade() throws Exception {
        Grade grade = new Grade();
        grade.setValue(7.5);

        SubjectDTO subjectDTO = new SubjectDTO(4L, "Geography");
        ExamDTO examDTO = new ExamDTO(4L, "Geography Exam", subjectDTO);
        GradeDTO dto = new GradeDTO(4L, 7.5, "Jane Doe", examDTO);

        when(gradeService.update(4L, grade)).thenReturn(dto);

        mockMvc.perform(put("/api/grades/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(grade)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(7.5))
                .andExpect(jsonPath("$.studentName").value("Jane Doe"))
                .andExpect(jsonPath("$.exam.title").value("Geography Exam"))
                .andExpect(jsonPath("$.exam.subject.name").value("Geography"));
    }

    @Test
    void testDeleteGrade() throws Exception {
        doNothing().when(gradeService).deleteById(5L);

        mockMvc.perform(delete("/api/grades/5"))
                .andExpect(status().isOk());
    }
}
