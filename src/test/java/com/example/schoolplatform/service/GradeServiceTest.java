package com.example.schoolplatform.service;

import com.example.schoolplatform.dto.GradeDTO;
import com.example.schoolplatform.entity.Exam;
import com.example.schoolplatform.entity.Grade;
import com.example.schoolplatform.entity.Student;
import com.example.schoolplatform.entity.Subject;
import com.example.schoolplatform.exception.ResourceNotFoundException;
import com.example.schoolplatform.repository.ExamRepository;
import com.example.schoolplatform.repository.GradeRepository;
import com.example.schoolplatform.repository.StudentRepository;
import com.example.schoolplatform.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private ExamRepository examRepository;

    @InjectMocks
    private GradeService gradeService;

    private Student student;
    private Exam exam;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        student = new Student("John Doe", "john@example.com");
        Subject subject = new Subject("Matemática");
        exam = new Exam("Final Exam", subject);
    }

    @Test
    @DisplayName("Deve retornar todas as notas como DTOs paginadas")
    void testFindAll() {
        Grade grade = new Grade(9.5, student, exam);
        when(gradeRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(grade)));

        var page = gradeService.findAll(0, 3, "id", "asc");
        List<GradeDTO> grades = page.getContent();

        assertEquals(1, grades.size());
        assertEquals(9.5, grades.get(0).value());
        assertEquals("John Doe", grades.get(0).studentName());
        assertEquals("Final Exam", grades.get(0).exam().title());
        assertEquals("Matemática", grades.get(0).exam().subject().name());

        System.out.println("Teste findAll executado: " + grades.get(0).value() + " / "
                + grades.get(0).studentName() + " / "
                + grades.get(0).exam().title() + " / "
                + grades.get(0).exam().subject().name());
    }

    @Test
    @DisplayName("Deve retornar nota por ID como DTO")
    void testFindById() {
        Grade grade = new Grade(9.5, student, exam);
        when(gradeRepository.findById(1L)).thenReturn(Optional.of(grade));

        GradeDTO dto = gradeService.findById(1L);

        assertEquals(9.5, dto.value());
        assertEquals("John Doe", dto.studentName());
        assertEquals("Final Exam", dto.exam().title());

        System.out.println("Teste findById executado: " + dto.value() + " / "
                + dto.studentName() + " / " + dto.exam().title());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nota não for encontrada")
    void testFindByIdNotFound() {
        when(gradeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> gradeService.findById(1L));
        System.out.println("Teste findByIdNotFound executado: Exceção lançada corretamente");
    }

    @Test
    @DisplayName("Deve salvar nota e retornar DTO")
    void testSave() {
        student.setId(1L);
        exam.setId(1L);
        exam.getSubject().setId(1L);

        Grade grade = new Grade(9.5, student, exam);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(exam.getSubject()));
        when(examRepository.findById(1L)).thenReturn(Optional.of(exam));

        when(gradeRepository.save(any(Grade.class))).thenReturn(grade);

        GradeDTO dto = gradeService.save(grade);

        assertEquals(9.5, dto.value());
        assertEquals("John Doe", dto.studentName());
        assertEquals("Final Exam", dto.exam().title());
        assertEquals("Matemática", dto.exam().subject().name());

        System.out.println("Teste save executado: " + dto.value() + " / "
                + dto.studentName() + " / " + dto.exam().title() + " / " + dto.exam().subject().name());
    }

    @Test
    @DisplayName("Deve atualizar nota existente e retornar DTO")
    void testUpdate() {
        Grade existingGrade = new Grade(8.0, student, exam);
        existingGrade.setId(1L);

        Student newStudent = new Student("Jane Doe", "jane@example.com");
        newStudent.setId(2L);
        Subject newSubject = new Subject("Física");
        newSubject.setId(2L);
        Exam newExam = new Exam("Midterm", newSubject);
        newExam.setId(2L);

        Grade gradeDetails = new Grade(9.0, newStudent, newExam);

        when(gradeRepository.findById(1L)).thenReturn(Optional.of(existingGrade));

        when(studentRepository.findById(2L)).thenReturn(Optional.of(newStudent));
        when(subjectRepository.findById(2L)).thenReturn(Optional.of(newSubject));
        when(examRepository.findById(2L)).thenReturn(Optional.of(newExam));

        when(gradeRepository.save(existingGrade)).thenReturn(existingGrade);

        GradeDTO dto = gradeService.update(1L, gradeDetails);

        assertEquals(9.0, dto.value());
        assertEquals("Jane Doe", dto.studentName());
        assertEquals("Midterm", dto.exam().title());
        assertEquals("Física", dto.exam().subject().name());

        System.out.println("Teste update executado: " + dto.value() + " / "
                + dto.studentName() + " / "
                + dto.exam().title() + " / " + dto.exam().subject().name());
    }

    @Test
    @DisplayName("Deve deletar nota por ID")
    void testDeleteById() {
        when(gradeRepository.existsById(1L)).thenReturn(true);
        gradeService.deleteById(1L);
        verify(gradeRepository, times(1)).deleteById(1L);

        System.out.println("Teste deleteById executado: ID 1 deletado");
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar nota inexistente")
    void testDeleteByIdNotFound() {
        when(gradeRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> gradeService.deleteById(1L));
        System.out.println("Teste deleteByIdNotFound executado: Exceção lançada corretamente");
    }
}
