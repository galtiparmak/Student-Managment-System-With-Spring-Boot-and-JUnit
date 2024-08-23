package com.example.studentsystem.ServiceTest;

import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Entity.Student;
import com.example.studentsystem.Repository.StudentRepository;
import com.example.studentsystem.Service.CourseService;
import com.example.studentsystem.Repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    public CourseServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse() {
        when(courseRepository.save(new Course(null, "Course1", "Description1", Arrays.asList("Step1")))).thenReturn(new Course(1L, "Course1", "Description1", Arrays.asList("Step1")));
        when(courseRepository.save(new Course(null, "Course2", "Description2", Arrays.asList("Step2")))).thenReturn(new Course(2L, "Course2", "Description2", Arrays.asList("Step2")));

        assertEquals(true, courseService.createCourse("Course1"));
        assertEquals(true, courseService.createCourse("Course2"));
    }

    @Test
    void testGetCourse() {
        Course course = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));

        Course course1 = courseService.getCourse(1L);
        assertEquals("Course1", course1.getName());
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        Course course2 = new Course(2L, "Course2", "Description2", Arrays.asList("Step2"));

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> courses = courseService.getCourses();
        assertEquals(2, courses.size());
        assertEquals("Course1", courses.get(0).getName());
    }

    @Test
    void testDeleteCourse() {
        // Arrange: Set up initial data
        Course course = new Course(1L, "Java", "Description", Arrays.asList("Step1"));
        Student student1 = new Student(1L, "John", "Student", Arrays.asList(course));
        Student student2 = new Student(2L, "Jane", "Student", Arrays.asList(course));
        List<Student> students = List.of(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);
        doNothing().when(courseRepository).deleteById(1L);

        // Act: Call the deleteCourse method
        boolean result = courseService.deleteCourse(1L);

        // Assert: Verify the course was removed from students and deleted
        assertTrue(result);
        verify(studentRepository, times(2)).save(any(Student.class)); // Each student should be saved after removing the course
        verify(courseRepository, times(1)).deleteById(1L); // Course should be deleted from the course repository
    }

    @Test
    void testUpdateCourseDescription() {
        Course course = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));

        assertEquals(true, courseService.updateCourseDescription(1L, "Description2"));
    }

    @Test
    void testUpdateCourseSteps() {
        Course course = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));

        assertEquals(true, courseService.updateCourseSteps(1L, Arrays.asList("Step2")));
    }
}

