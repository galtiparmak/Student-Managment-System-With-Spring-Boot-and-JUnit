package com.example.studentsystem.ServiceTest;

import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Entity.Student;
import com.example.studentsystem.Repository.CourseRepository;
import com.example.studentsystem.Repository.StudentRepository;
import com.example.studentsystem.Service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    public StudentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    // Add your tests here
    @Test
    void testCreateStudent() {
        when(studentRepository.save(new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1")))))).thenReturn(new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1")))));
        when(studentRepository.save(new Student(2L, "Jane", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step2")))))).thenReturn(new Student(2L, "Jane", "Student", Arrays.asList(new Course(2L, "Java", "Description", Arrays.asList("Step1")))));

        assertEquals(true, studentService.createStudent("John"));
        assertEquals(true, studentService.createStudent("Jane"));
    }

    @Test
    void testGetStudent() {
        Student student = new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1"))));
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        Student student1 = studentService.getStudent(1L);
        assertEquals("John", student1.getName());
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1"))));
        Student student2 = new Student(2L, "Jane", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step2"))));

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<Student> students = studentService.getStudents();
        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getName());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1"))));
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        assertEquals(true, studentService.updateStudent(1L, "John"));
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1"))));
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        assertEquals(true, studentService.deleteStudent(1L));
    }

    @Test
    void testAddCourse() {
        // Setup initial student and course
        Student student = new Student(1L, "John", "Student", new ArrayList<>()); // Start with an empty course list
        Course course = new Course(1L, "Java", "Description", Arrays.asList("Step1"));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Call the service method
        boolean result = studentService.addCourse(1L, 1L);

        // Assertions
        assertTrue(result);  // Ensure the method returned true
        assertEquals(1, student.getCourses().size());  // Ensure the course was added
        assertEquals("Java", student.getCourses().get(0).getName());  // Ensure the correct course was added
    }


    @Test
    void testRemoveCourse() {
        // Setup initial student and course
        Course course = new Course(1L, "Java", "Description", Arrays.asList("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(Arrays.asList(course)));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Call the service method
        boolean result = studentService.removeCourse(1L, 1L);

        // Assertions
        assertTrue(result);  // Ensure the method returned true
        assertEquals(0, student.getCourses().size());  // Ensure the course was removed
    }

}
