package com.example.schoolplatform.controller;

import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.service.SubjectService;
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
        SubjectDTO dto = new SubjectDTO(1L, "Mathematics");
        when(subjectService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Mathematics"));
    }

    @Test
    void testGetSubjectById() throws Exception {
        SubjectDTO dto = new SubjectDTO(1L, "History");
        when(subjectService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("History"));
    }

    @Test
    void testCreateSubject() throws Exception {
        Subject subject = new Subject("Physics");
        SubjectDTO dto = new SubjectDTO(1L, "Physics");

        when(subjectService.save(any(Subject.class))).thenReturn(dto);

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Physics"));
    }

    @Test
    void testUpdateSubject() throws Exception {
        Subject subject = new Subject("Chemistry");
        SubjectDTO dto = new SubjectDTO(1L, "Chemistry");

        when(subjectService.update(1L, subject)).thenReturn(dto);

        mockMvc.perform(put("/api/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Chemistry"));
    }

    @Test
    void testDeleteSubject() throws Exception {
        doNothing().when(subjectService).deleteById(1L);

        mockMvc.perform(delete("/api/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
