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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        when(courseRepository.save(new Course(null, "Course1", "Description1", List.of("Step1")))).thenReturn(new Course(1L, "Course1", "Description1", Arrays.asList("Step1")));
        when(courseRepository.save(new Course(null, "Course2", "Description2", List.of("Step2")))).thenReturn(new Course(2L, "Course2", "Description2", Arrays.asList("Step2")));

        assertTrue(courseService.createCourse("Course1"));
        assertTrue(courseService.createCourse("Course2"));
    }

    @Test
    void testCreateCourseWhenCourseNameIsNull() {
        assertFalse(courseService.createCourse(null));
    }

    @Test
    void testCreateCourseWhenCourseNameIsEmpty() {
        assertFalse(courseService.createCourse(""));
    }

    @Test
    void testCreateCourseWhenCourseAlreadyExists() {
        when(courseRepository.findByName("Course1")).thenReturn(new Course (1L, "Course1", "Description1", List.of("Step1")));
        assertFalse(courseService.createCourse("Course1"));
    }

    @Test
    void testCreateCourseWhenException() {
        when(courseRepository.save(any(Course.class))).thenThrow(new RuntimeException("Database error"));

        assertFalse(courseService.createCourse("Course1"));
    }

    @Test
    void testGetCourse() {
        Course course = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course course1 = courseService.getCourse(1L);
        assertEquals("Course1", course1.getName());
    }

    @Test
    void testGetCourseWhenCourseDoesNotExist() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(courseService.getCourse(1L));
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
    public void testDeleteCourseWhenIdIsNull() {
        boolean result = courseService.deleteCourse(null);

        assertFalse(result);
    }

    @Test
    public void testDeleteCourseWhenIdIsNotNullAndCourseExists() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);

        Student student1 = new Student();
        student1.setId(1L);
        List<Course> courses1 = new ArrayList<>();
        courses1.add(course);
        student1.setCourses(courses1);

        Student student2 = new Student();
        student2.setId(2L);
        List<Course> courses2 = new ArrayList<>();
        courses2.add(course);
        student2.setCourses(courses2);

        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        when(studentRepository.findAll()).thenReturn(students);

        boolean result = courseService.deleteCourse(courseId);

        assertTrue(result);
        assertTrue(student1.getCourses().isEmpty());
        assertTrue(student2.getCourses().isEmpty());
        verify(studentRepository, times(2)).save(any(Student.class));
        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    public void testDeleteCourseWhenExceptionOccurs() {
        Long courseId = 1L;

        doThrow(new RuntimeException("Database error")).when(courseRepository).deleteById(courseId);

        boolean result = courseService.deleteCourse(courseId);

        assertFalse(result);
        verify(studentRepository, times(1)).findAll();
        verify(courseRepository, times(1)).deleteById(courseId);
    }

    @Test
    void testUpdateCourseDescription() {
        Course course = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));

        assertEquals(true, courseService.updateCourseDescription(1L, "Description2"));
    }

    @Test
    void testUpdateCourseDescriptionWhenDescriptionIsNull() {
        assertFalse(courseService.updateCourseDescription(1L, null));
    }

    @Test
    void testUpdateCourseDescriptionWhenDescriptionIsEmpty() {
        assertFalse(courseService.updateCourseDescription(1L, ""));
    }

    @Test
    void testUpdateCourseDescriptionWhenCourseDoesNotExist() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(courseService.updateCourseDescription(1L, "Description2"));
    }

    @Test
    void testUpdateCourseDescriptionWhenException() {
        when(courseRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertFalse(courseService.updateCourseDescription(1L, "Description2"));
    }

    @Test
    void testUpdateCourseSteps() {
        Course course = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        when(courseRepository.findById(1L)).thenReturn(java.util.Optional.of(course));

        assertEquals(true, courseService.updateCourseSteps(1L, Arrays.asList("Step2")));
    }

    @Test
    void testUpdateCourseStepsWhenStepsIsNull() {
        assertFalse(courseService.updateCourseSteps(1L, null));
    }

    @Test
    void testUpdateCourseStepsWhenStepsIsEmpty() {
        when(courseService.updateCourseSteps(1L, new ArrayList<>())).thenReturn(false);

        assertFalse(courseService.updateCourseSteps(1L, new ArrayList<>()));
    }

    @Test
    void testUpdateCourseStepsWhenCourseDoesNotExist() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(courseService.updateCourseSteps(1L, Arrays.asList("Step2")));
    }

    @Test
    void testUpdateCourseStepsWhenException() {
        when(courseRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertFalse(courseService.updateCourseSteps(1L, Arrays.asList("Step2")));
    }

    @Test
    void testIsCourseExist() {
        Course course = new Course(1L, "Course1", "Description1", Arrays.asList("Step1"));
        when(courseRepository.findByName("Course1")).thenReturn(course);

        assertTrue(courseService.isCourseExist("Course1"));
    }

    @Test
    void testIsCourseExistWhenCoursesAreNull() {
        when(courseRepository.findAll()).thenReturn(null);

        assertFalse(courseService.isCourseExist("Course1"));
    }

    @Test
    void testIsCourseExistsWhenCoursesAreEmpty() {
        when(courseRepository.findAll()).thenReturn(new ArrayList<>());

        assertFalse(courseService.isCourseExist("Course1"));
    }

    @Test
    void testIsCourseExistsWhenException() {
        when(courseRepository.findByName("Course1")).thenThrow(new RuntimeException("Database error"));

        assertFalse(courseService.isCourseExist("Course1"));
    }
}

