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

import static org.junit.jupiter.api.Assertions.*;
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
        when(studentRepository.save(new Student(1L, "John", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step1")))))).thenReturn(new Student(1L, "John", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step1")))));
        when(studentRepository.save(new Student(2L, "Jane", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step2")))))).thenReturn(new Student(2L, "Jane", "Student", List.of(new Course(2L, "Java", "Description", List.of("Step1")))));

        assertTrue(studentService.createStudent("John"));
        assertTrue(studentService.createStudent("Jane"));
    }
    @Test
    void testCreateStudentWithNullName() {
        assertFalse(studentService.createStudent(null));
    }

    @Test
    void testCreateStudentWithEmptyName() {
        assertFalse(studentService.createStudent("   "));
    }

    @Test
    void testCreateStudentWithException() {
        // Simulate an exception being thrown when saving the student
        when(studentRepository.save(any(Student.class))).thenThrow(new RuntimeException("Database error"));
        assertFalse(studentService.createStudent("John"));
    }

    @Test
    void testGetStudent() {
        Student student = new Student(1L, "John", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step1"))));
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        Student student1 = studentService.getStudent(1L);
        assertEquals("John", student1.getName());
    }

    @Test
    void testGetStudentWhenNotFound() {
        // Simulate an empty Optional being returned when searching for a student by ID
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(studentService.getStudent(1L));
    }


    @Test
    void testGetAllStudents() {
        Student student1 = new Student(1L, "John", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step1"))));
        Student student2 = new Student(2L, "Jane", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step2"))));

        when(studentRepository.findAll()).thenReturn(Arrays.asList(student1, student2));

        List<Student> students = studentService.getStudents();
        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getName());
    }

    @Test
    void testUpdateStudent() {
        Student student = new Student(1L, "John", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step1"))));
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        assertTrue(studentService.updateStudent(1L, "John"));
    }

    @Test
    void testUpdateStudentWithNullId() {
        assertFalse(studentService.updateStudent(null, "John"));
    }

    @Test
    void testUpdateStudentWithNullDescription() {
        assertFalse(studentService.updateStudent(1L, null));
    }

    @Test
    void testUpdateStudentWithEmptyDescription() {
        assertFalse(studentService.updateStudent(1L, "   "));
    }

    @Test
    void testUpdateStudentWhenNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(studentService.updateStudent(1L, "John"));
    }

    @Test
    void testUpdateStudentWithException() {
        when(studentRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertFalse(studentService.updateStudent(1L, "John"));
    }

    @Test
    void testDeleteStudent() {
        Student student = new Student(1L, "John", "Student", List.of(new Course(1L, "Java", "Description", List.of("Step1"))));
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));

        assertTrue(studentService.deleteStudent(1L));
    }

    @Test
    void testDeleteStudentWhenIdNull() {
        assertFalse(studentService.deleteStudent(null));
    }

    @Test
    void testDeleteStudentWhenNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(studentService.deleteStudent(1L));
    }

    @Test
    void testDeleteStudentWhenException() {
        when(studentRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));
        assertFalse(studentService.deleteStudent(1L));
    }

    @Test
    void testAddCourse() {
        // Setup initial student and course
        Student student = new Student(1L, "John", "Student", new ArrayList<>()); // Start with an empty course list
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));

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
    void testAddCourseWhenStudentIdNull() {
        assertFalse(studentService.addCourse(null, 1L));
    }

    @Test
    void testAddCourseWhenCourseIdNull() {
        assertFalse(studentService.addCourse(1L, null));
    }

    @Test
    void testAddCourseWhenStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(studentService.addCourse(1L, 1L));
    }

    @Test
    void testAddCourseWhenCourseNotFound() {
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        assertFalse(studentService.addCourse(1L, 2L));
    }

    @Test
    void testAddCourseWhenCourseIsAlreadyEnrolled() {
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertFalse(studentService.addCourse(1L, 1L));
    }

    @Test
    void testAddCourseWhenException() {
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenThrow(new RuntimeException("Database error"));

        assertFalse(studentService.addCourse(1L, 1L));
    }

    @Test
    void testRemoveCourse() {
        // Setup initial student and course
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Call the service method
        boolean result = studentService.removeCourse(1L, 1L);

        // Assertions
        assertTrue(result);  // Ensure the method returned true
        assertEquals(0, student.getCourses().size());  // Ensure the course was removed
    }

    @Test
    void testRemoveCourseWhenStudentIdNull() {
        assertFalse(studentService.removeCourse(null, 1L));
    }

    @Test
    void testRemoveCourseWhenCourseIdNull() {
        assertFalse(studentService.removeCourse(1L, null));
    }

    @Test
    void testRemoveCourseWhenStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(studentService.removeCourse(1L, 1L));
    }

    @Test
    void testRemoveCourseWhenCourseNotFound() {
        // Setup initial student and course
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertFalse(studentService.removeCourse(1L, 2L));
    }

    @Test
    void testRemoveCourseWhenException() {
        // Setup initial student and course
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenThrow(new RuntimeException("Database error"));

        assertFalse(studentService.removeCourse(1L, 1L));
    }

    @Test
    void testRemoveCourseWhenCourseNotInList() {
        // Setup initial student and course
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        assertFalse(studentService.removeCourse(1L, 2L));
    }

    @Test
    void testRemoveCourseWhenCourseListEmpty() {
        // Setup initial student with an empty course list
        Student student = new Student(1L, "John", "Student", new ArrayList<>());

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        assertFalse(studentService.removeCourse(1L, 1L));
    }

    @Test
    void testRemoveCourseWhenCourseListNull() {
        // Setup initial student with a null course list
        Student student = new Student(1L, "John", "Student", null);

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        assertFalse(studentService.removeCourse(1L, 1L));
    }

    @Test
    void testIsCourseEnrolled() {
        // Setup initial student and course
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertTrue(studentService.isCourseEnrolled(1L, 1L));
    }

    @Test
    void testIsCourseEnrolledWhenStudentIdNull() {
        assertFalse(studentService.isCourseEnrolled(null, 1L));
    }

    @Test
    void testIsCourseEnrolledWhenCourseIdNull() {
        assertFalse(studentService.isCourseEnrolled(1L, null));
    }

    @Test
    void testIsCourseEnrolledWhenStudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(studentService.isCourseEnrolled(1L, 1L));
    }

    @Test
    void testIsCourseEnrolledWhenCourseListNull() {
        // Setup initial student with a null course list
        Student student = new Student(1L, "John", "Student", null);

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertFalse(studentService.isCourseEnrolled(1L, 1L));
    }

    @Test
    void testIsCourseEnrolledWhenCourseListEmpty() {
        // Setup initial student with an empty course list
        Student student = new Student(1L, "John", "Student", new ArrayList<>());

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertFalse(studentService.isCourseEnrolled(1L, 1L));
    }

    @Test
    void testIsCourseEnrolledWhenException() {
        // Setup initial student and course
        Course course = new Course(1L, "Java", "Description", List.of("Step1"));
        Student student = new Student(1L, "John", "Student", new ArrayList<>(List.of(course)));

        // Mocking the repositories
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Simulate an exception being thrown when checking if the course is enrolled
        when(studentRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertFalse(studentService.isCourseEnrolled(1L, 1L));
    }
}
