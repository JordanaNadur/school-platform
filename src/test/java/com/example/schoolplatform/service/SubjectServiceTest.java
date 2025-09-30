package com.example.schoolplatform.service;

import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.repository.SubjectRepository;
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

public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todas as matérias")
    void testFindAll() {
        Subject subject = new Subject("Matemática");
        when(subjectRepository.findAll()).thenReturn(Arrays.asList(subject));
        List<Subject> subjects = subjectService.findAll();
        assertEquals(1, subjects.size());
        assertEquals("Matemática", subjects.get(0).getName());
    }

    @Test
    @DisplayName("Deve retornar matéria por ID")
    void testFindById() {
        Subject subject = new Subject("Matemática");
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        Subject found = subjectService.findById(1L);
        assertEquals("Matemática", found.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção quando matéria não for encontrada por ID")
    void testFindByIdNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subjectService.findById(1L));
    }

    @Test
    @DisplayName("Deve salvar uma nova matéria")
    void testSave() {
        Subject subject = new Subject("Matemática");
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);
        Subject saved = subjectService.save(subject);
        assertEquals("Matemática", saved.getName());
    }

    @Test
    @DisplayName("Deve atualizar uma matéria existente")
    void testUpdate() {
        Subject subject = new Subject("Matemática");
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);
        Subject updated = subjectService.update(1L, new Subject("Física"));
        assertEquals("Física", updated.getName());
    }

    @Test
    @DisplayName("Deve deletar uma matéria por ID")
    void testDeleteById() {
        when(subjectRepository.existsById(1L)).thenReturn(true);
        subjectService.deleteById(1L);
        verify(subjectRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar matéria inexistente")
    void testDeleteByIdNotFound() {
        when(subjectRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> subjectService.deleteById(1L));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar matéria nula")
    void testSaveNullSubject() {
        assertThrows(NullPointerException.class, () -> subjectService.save(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar matéria nula")
    void testUpdateNullSubject() {
        assertThrows(NullPointerException.class, () -> subjectService.update(1L, null));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver matérias")
    void testFindAllEmpty() {
        when(subjectRepository.findAll()).thenReturn(Collections.emptyList());
        List<Subject> subjects = subjectService.findAll();
        assertTrue(subjects.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao ocorrer erro no repositório ao buscar todos")
    void testFindAllRepositoryException() {
        when(subjectRepository.findAll()).thenThrow(new RuntimeException("Erro no banco"));
        assertThrows(RuntimeException.class, () -> subjectService.findAll());
    }
}
