package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.SubjectDTO;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.repository.SubjectRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        when(subjectRepository.findAll()).thenReturn(List.of(subject));

        List<SubjectDTO> subjects = subjectService.findAll();

        assertEquals(1, subjects.size());
        assertEquals("Matemática", subjects.get(0).name());

        System.out.println("Teste findAll executado: " + subjects.get(0).name());
    }

    @Test
    @DisplayName("Deve retornar matéria por ID")
    void testFindById() {
        Subject subject = new Subject("Matemática");
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));

        SubjectDTO dto = subjectService.findById(1L);

        assertEquals("Matemática", dto.name());

        System.out.println("Teste findById executado: " + dto.name());
    }

    @Test
    @DisplayName("Deve lançar exceção quando matéria não for encontrada")
    void testFindByIdNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> subjectService.findById(1L));

        System.out.println("Teste findByIdNotFound executado: exceção ResourceNotFoundException lançada com sucesso");
    }

    @Test
    @DisplayName("Deve salvar uma nova matéria")
    void testSave() {
        Subject subject = new Subject("Matemática");
        when(subjectRepository.save(subject)).thenReturn(subject);

        SubjectDTO dto = subjectService.save(subject);

        assertEquals("Matemática", dto.name());

        System.out.println("Teste save executado: " + dto.name());
    }

    @Test
    @DisplayName("Deve atualizar matéria existente e retornar DTO")
    void testUpdate() {
        Subject subject = new Subject("Matemática");
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectRepository.save(subject)).thenReturn(subject);

        SubjectDTO dto = subjectService.update(1L, new Subject("Física"));

        assertEquals("Física", dto.name());

        System.out.println("Teste update executado: " + dto.name());
    }

    @Test
    @DisplayName("Deve deletar matéria por ID")
    void testDeleteById() {
        when(subjectRepository.existsById(1L)).thenReturn(true);
        subjectService.deleteById(1L);
        verify(subjectRepository, times(1)).deleteById(1L);

        System.out.println("Teste deleteById executado com sucesso");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar matéria inexistente")
    void testDeleteByIdNotFound() {
        when(subjectRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> subjectService.deleteById(1L));

        System.out.println("Teste deleteByIdNotFound executado: exceção ResourceNotFoundException lançada com sucesso");
    }
}
