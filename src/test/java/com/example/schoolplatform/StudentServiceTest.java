package com.example.schoolplatform.service;

import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.repository.StudentRepository;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
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
    void testFindAll() {
        Student student = new Student("John Doe", "john@example.com");
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));
        List<Student> students = studentService.findAll();
        assertEquals(1, students.size());
        assertEquals("John Doe", students.get(0).getName());
    }

    @Test
    void testFindById() {
        Student student = new Student("John Doe", "john@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        Student found = studentService.findById(1L);
        assertEquals("John Doe", found.getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> studentService.findById(1L));
    }

    @Test
    void testSave() {
        Student student = new Student("John Doe", "john@example.com");
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        Student saved = studentService.save(student);
        assertEquals("John Doe", saved.getName());
    }

    @Test
    void testUpdate() {
        Student student = new Student("John Doe", "john@example.com");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        Student updated = studentService.update(1L, new Student("Jane Doe", "jane@example.com"));
        assertEquals("Jane Doe", updated.getName());
    }

    @Test
    void testDeleteById() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        studentService.deleteById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteById(1L));
    }
}
