package com.example.schoolplatform.controller;

import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.service.SubjectService;
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

@WebMvcTest(SubjectController.class)
public class SubjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllSubjects() throws Exception {
        Subject subject = new Subject("Mathematics");
        List<Subject> subjects = Arrays.asList(subject);
        when(subjectService.findAll()).thenReturn(subjects);
        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mathematics"));
    }

    @Test
    void testGetSubjectById() throws Exception {
        Subject subject = new Subject("Mathematics");
        when(subjectService.findById(1L)).thenReturn(subject);
        mockMvc.perform(get("/api/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mathematics"));
    }

    @Test
    void testCreateSubject() throws Exception {
        Subject subject = new Subject("Mathematics");
        when(subjectService.save(any(Subject.class))).thenReturn(subject);
        mockMvc.perform(post("/api/subjects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mathematics"));
    }

    @Test
    void testUpdateSubject() throws Exception {
        Subject subject = new Subject("Physics");
        when(subjectService.update(1L, any(Subject.class))).thenReturn(subject);
        mockMvc.perform(put("/api/subjects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Physics"));
    }

    @Test
    void testDeleteSubject() throws Exception {
        mockMvc.perform(delete("/api/subjects/1"))
                .andExpect(status().isNoContent());
    }
}
