package com.example.schoolplatform.service;

import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.repository.GradeRepository;
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

public class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private GradeService gradeService;

    private Student student;
    private Exam exam;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student("John Doe", "john@example.com");
        Subject subject = new Subject("Mathematics");
        exam = new Exam("Final Exam", subject);
    }

    @Test
    @DisplayName("Deve retornar todas as notas")
    void testFindAll() {
        Grade grade = new Grade(9.5, student, exam);
        when(gradeRepository.findAll()).thenReturn(Arrays.asList(grade));
        List<Grade> grades = gradeService.findAll();
        assertEquals(1, grades.size());
        assertEquals(9.5, grades.get(0).getValue());
        assertEquals("John Doe", grades.get(0).getStudent().getName());
        assertEquals("Final Exam", grades.get(0).getExam().getTitle());
    }

    @Test
    @DisplayName("Deve retornar nota por ID")
    void testFindById() {
        Grade grade = new Grade(9.5, student, exam);
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        Grade found = gradeService.findById(1L);
        assertEquals(9.5, found.getValue());
        assertEquals("John Doe", found.getStudent().getName());
        assertEquals("Final Exam", found.getExam().getTitle());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nota não for encontrada por ID")
    void testFindByIdNotFound() {
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gradeService.findById(1L));
    }

    @Test
    @DisplayName("Deve salvar uma nova nota")
    void testSave() {
        Grade grade = new Grade(9.5, student, exam);
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);
        Grade saved = gradeService.save(grade);
        assertEquals(9.5, saved.getValue());
        assertEquals("John Doe", saved.getStudent().getName());
        assertEquals("Final Exam", saved.getExam().getTitle());
    }

    @Test
    @DisplayName("Deve atualizar uma nota existente")
    void testUpdate() {
        Grade grade = new Grade(8.0, student, exam);
        Student newStudent = new Student("Jane Doe", "jane@example.com");
        Subject newSubject = new Subject("Physics");
        Exam newExam = new Exam("Midterm", newSubject);
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));
        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);
        Grade updated = gradeService.update(1L, new Grade(9.0, newStudent, newExam));
        assertEquals(9.0, updated.getValue());
        assertEquals("Jane Doe", updated.getStudent().getName());
        assertEquals("Midterm", updated.getExam().getTitle());
    }

    @Test
    @DisplayName("Deve deletar uma nota por ID")
    void testDeleteById() {
        when(gradeRepository.existsById(1L)).thenReturn(true);
        gradeService.deleteById(1L);
        verify(gradeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar nota inexistente")
    void testDeleteByIdNotFound() {
        when(gradeRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> gradeService.deleteById(1L));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar nota nula")
    void testSaveNullGrade() {
        assertThrows(NullPointerException.class, () -> gradeService.save(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar nota nula")
    void testUpdateNullGrade() {
        assertThrows(NullPointerException.class, () -> gradeService.update(1L, null));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver notas")
    void testFindAllEmpty() {
        when(gradeRepository.findAll()).thenReturn(Collections.emptyList());
        List<Grade> grades = gradeService.findAll();
        assertTrue(grades.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao ocorrer erro no repositório ao buscar todos")
    void testFindAllRepositoryException() {
        when(gradeRepository.findAll()).thenThrow(new RuntimeException("Erro no banco"));
        assertThrows(RuntimeException.class, () -> gradeService.findAll());
    }
}
