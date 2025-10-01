package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.StudentDTO;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.GradeRepository;
import com.example.schoolplatform.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private GradeRepository gradeRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todos os estudantes como DTOs paginados")
    void testFindAll() {
        Student student = new Student("John Doe", "john@example.com");
        Page<Student> studentPage = new PageImpl<>(List.of(student));

        when(studentRepository.findAll(any(Pageable.class))).thenReturn(studentPage);

        var page = studentService.findAll(0, 3, "id", "asc");
        List<StudentDTO> students = page.getContent();

        assertEquals(1, students.size());
        assertEquals("John Doe", students.get(0).name());
        assertEquals("john@example.com", students.get(0).email());

        System.out.println("Teste findAll executado: " + students.get(0).name() + " / " + students.get(0).email());
    }

    @Test
    @DisplayName("Deve retornar estudante por ID como DTO")
    void testFindById() {
        Student student = new Student("John Doe", "john@example.com");
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentDTO dto = studentService.findById(1L);

        assertEquals("John Doe", dto.name());
        assertEquals("john@example.com", dto.email());

        System.out.println("Teste findById executado: " + dto.name() + " / " + dto.email());
    }

    @Test
    @DisplayName("Deve lançar exceção quando estudante não for encontrado por ID")
    void testFindByIdNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.findById(1L));

        System.out.println("Teste findByIdNotFound executado: exceção ResourceNotFoundException lançada com sucesso");
    }

    @Test
    @DisplayName("Deve salvar um novo estudante e retornar DTO")
    void testSave() {
        Student student = new Student("John Doe", "john@example.com");
        student.setId(1L);
        when(studentRepository.save(student)).thenReturn(student);

        StudentDTO dto = studentService.save(student);

        assertEquals("John Doe", dto.name());
        assertEquals("john@example.com", dto.email());

        System.out.println("Teste save executado: " + dto.name() + " / " + dto.email());
    }

    @Test
    @DisplayName("Deve atualizar um estudante existente e retornar DTO")
    void testUpdate() {
        Student existingStudent = new Student("John Doe", "john@example.com");
        existingStudent.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(existingStudent)).thenReturn(existingStudent);

        Student updatedData = new Student("Jane Doe", "jane@example.com");
        updatedData.setId(1L);

        StudentDTO dto = studentService.update(1L, updatedData);

        assertEquals("Jane Doe", dto.name());
        assertEquals("jane@example.com", dto.email());

        System.out.println("Teste update executado: " + dto.name() + " / " + dto.email());
    }

    @Test
    @DisplayName("Deve deletar estudante por ID")
    void testDeleteById() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        studentService.deleteById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
        System.out.println("Teste deleteById executado: estudante 1L deletado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar estudante inexistente")
    void testDeleteByIdNotFound() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> studentService.deleteById(1L));

        System.out.println("Teste deleteByIdNotFound executado: exceção ResourceNotFoundException lançada com sucesso");
    }
}
