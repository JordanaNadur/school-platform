package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.ExamDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.ExamRepository;
import com.example.schoolplatform.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private ExamService examService;

    private Subject subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subject = new Subject("Matemática");
        subject.setId(1L);
    }

    @Test
    @DisplayName("Deve retornar todos os exames como DTOs")
    void testFindAll() {
        Exam exam = new Exam("Final Exam", subject);
        when(examRepository.findAll()).thenReturn(Arrays.asList(exam));

        List<ExamDTO> exams = examService.findAll();

        assertEquals(1, exams.size());
        assertEquals("Final Exam", exams.get(0).title());
        assertEquals("Matemática", exams.get(0).subject().name());

        System.out.println("Teste findAll executado: " + exams.get(0).title() + " / " + exams.get(0).subject().name());
    }

    @Test
    @DisplayName("Deve retornar exame por ID como DTO")
    void testFindById() {
        Exam exam = new Exam("Final Exam", subject);
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        ExamDTO dto = examService.findById(1L);

        assertEquals("Final Exam", dto.title());
        assertEquals("Matemática", dto.subject().name());

        System.out.println("Teste findById executado: " + dto.title() + " / " + dto.subject().name());
    }

    @Test
    @DisplayName("Deve lançar exceção quando exame não for encontrado por ID")
    void testFindByIdNotFound() {
        when(examRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> examService.findById(1L));
        System.out.println("Teste findByIdNotFound executado: Exceção lançada corretamente");
    }

    @Test
    @DisplayName("Deve salvar exame e retornar DTO")
    void testSave() {
        Exam exam = new Exam("Final Exam", subject);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        ExamDTO dto = examService.save(exam);

        assertEquals("Final Exam", dto.title());
        assertEquals("Matemática", dto.subject().name());

        System.out.println("Teste save executado: " + dto.title() + " / " + dto.subject().name());
    }

    @Test
    @DisplayName("Deve atualizar exame existente e retornar DTO")
    void testUpdate() {
        Exam existingExam = new Exam("Midterm Exam", subject);
        existingExam.setId(1L);

        Subject newSubject = new Subject("Física");
        newSubject.setId(2L);

        when(subjectRepository.findById(2L)).thenReturn(Optional.of(newSubject));
        when(examRepository.findById(1L)).thenReturn(Optional.of(existingExam));
        when(examRepository.save(existingExam)).thenReturn(existingExam);

        Exam examDetails = new Exam("Final Exam", newSubject);
        ExamDTO dto = examService.update(1L, examDetails);

        assertEquals("Final Exam", dto.title());
        assertEquals("Física", dto.subject().name());

        System.out.println("Teste update executado: " + dto.title() + " / " + dto.subject().name());
    }

    @Test
    @DisplayName("Deve deletar exame por ID")
    void testDeleteById() {
        when(examRepository.existsById(1L)).thenReturn(true);
        examService.deleteById(1L);
        verify(examRepository, times(1)).deleteById(1L);

        System.out.println("Teste deleteById executado: ID 1 deletado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar exame inexistente")
    void testDeleteByIdNotFound() {
        when(examRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> examService.deleteById(1L));
        System.out.println("Teste deleteByIdNotFound executado: Exceção lançada corretamente");
    }
}
