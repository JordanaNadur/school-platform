package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.repository.StudentRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todos os estudantes")
    void testFindAll() {
        Student student = new Student("John Doe", "john@example.com");
        Page<Student> studentPage = new PageImpl<>(Arrays.asList(student));
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(studentPage);
        Page<StudentDTO> students = studentService.findAll(0, 10, "id", "asc");
        assertEquals(1, students.getContent().size());
        assertEquals("John Doe", students.getContent().get(0).name());
        assertEquals("john@example.com", students.getContent().get(0).email());
    }

    @Test
    @DisplayName("Deve retornar estudante por ID")
    void testFindById() {
        Student student = new Student("John Doe", "john@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        StudentDTO found = studentService.findById(1L);
        assertEquals("John Doe", found.name());
        assertEquals("john@example.com", found.email());
    }

    @Test
    @DisplayName("Deve lançar exceção quando estudante não for encontrado por ID")
    void testFindByIdNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> studentService.findById(1L));
    }

    @Test
    @DisplayName("Deve salvar um novo estudante")
    void testSave() {
        StudentDTO studentDTO = new StudentDTO(null, "John Doe", "john@example.com", List.of());
        Student student = new Student("John Doe", "john@example.com");
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        StudentDTO saved = studentService.save(studentDTO);
        assertEquals("John Doe", saved.name());
        assertEquals("john@example.com", saved.email());
    }

    @Test
    @DisplayName("Deve atualizar um estudante existente")
    void testUpdate() {
        Student student = new Student("John Doe", "john@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        StudentDTO updated = studentService.update(1L, new StudentDTO(null, "Jane Doe", "jane@example.com", List.of()));
        assertEquals("Jane Doe", updated.name());
        assertEquals("jane@example.com", updated.email());
    }

    @Test
    @DisplayName("Deve deletar um estudante por ID")
    void testDeleteById() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        studentService.deleteById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar estudante inexistente")
    void testDeleteByIdNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteById(1L));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar estudante nulo")
    void testSaveNullStudent() {
        assertThrows(NullPointerException.class, () -> studentService.save(null));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar estudante nulo")
    void testUpdateNullStudent() {
        assertThrows(NullPointerException.class, () -> studentService.update(1L, null));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver estudantes")
    void testFindAllEmpty() {
        Page<Student> emptyPage = new PageImpl<>(Collections.emptyList());
        when(studentRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
        Page<StudentDTO> students = studentService.findAll(0, 10, "id", "asc");
        assertTrue(students.getContent().isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção ao ocorrer erro no repositório ao buscar todos")
    void testFindAllRepositoryException() {
        when(studentRepository.findAll(any(Pageable.class))).thenThrow(new RuntimeException("Erro no banco"));
        assertThrows(RuntimeException.class, () -> studentService.findAll(0, 10, "id", "asc"));
    }
}
