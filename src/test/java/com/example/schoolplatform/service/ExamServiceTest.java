package com.example.schoolplatform.service;

import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.repository.ExamRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private ExamService examService;

    private Subject subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new Subject("Mathematics");
    }

    @Test
    @DisplayName("Deve retornar todos os exames")
    void testFindAll() {
        Exam exam = new Exam("Final Exam", subject);
        when(examRepository.findAll()).thenReturn(Arrays.asList(exam));
        List<Exam> exams = examService.findAll();
        assertEquals(1, exams.size());
        assertEquals("Final Exam", exams.get(0).getTitle());
        assertEquals("Mathematics", exams.get(0).getSubject().getName());
    }

    @Test
    @DisplayName("Deve retornar exame por ID")
    void testFindById() {
        Exam exam = new Exam("Final Exam", subject);
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        Exam found = examService.findById(1L);
        assertEquals("Final Exam", found.getTitle());
        assertEquals("Mathematics", found.getSubject().getName());
    }

    @Test
    @DisplayName("Deve lançar exceção quando exame não for encontrado por ID")
    void testFindByIdNotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> examService.findById(1L));
    }

    @Test
    @DisplayName("Deve salvar um novo exame")
    void testSave() {
        Exam exam = new Exam("Final Exam", subject);
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        Exam saved = examService.save(exam);
        assertEquals("Final Exam", saved.getTitle());
        assertEquals("Mathematics", saved.getSubject().getName());
    }

    @Test
    @DisplayName("Deve atualizar um exame existente")
    void testUpdate() {
        Exam exam = new Exam("Midterm Exam", subject);
        Subject newSubject = new Subject("Physics");
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        Exam updated = examService.update(1L, new Exam("Final Exam", newSubject));
        assertEquals("Final Exam", updated.getTitle());
        assertEquals("Physics", updated.getSubject().getName());
    }

    @Test
    @DisplayName("Deve deletar um exame por ID")
    void testDeleteById() {
        when(examRepository.existsById(1L)).thenReturn(true);
        examService.deleteById(1L);
        verify(examRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar exame inexistente")
    void testDeleteByIdNotFound() {
        when(examRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> examService.deleteById(1L));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar exame nulo")
    void testSaveNullExam() {
        assertThrows(NullPointerException.class, () -> examService.save(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar exame nulo")
    void testUpdateNullExam() {
        assertThrows(NullPointerException.class, () -> examService.update(1L, null));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver exames")
    void testFindAllEmpty() {
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        List<Exam> exams = examService.findAll();
        assertTrue(exams.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao ocorrer erro no repositório ao buscar todos")
    void testFindAllRepositoryException() {
        when(examRepository.findAll()).thenThrow(new RuntimeException("Erro no banco"));
        assertThrows(RuntimeException.class, () -> examService.findAll());
    }
}
