package com.example.studentsystem.ControllerTest;

import com.example.studentsystem.Controller.CourseController;
import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    // Write your tests here
    @Test
    void testCreateCourse() throws Exception {
        when(courseService.createCourse("Java")).thenReturn(true);

        mockMvc.perform(post("/api/course/create/Java"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateCourseBadRequest() throws Exception {
        when(courseService.createCourse("Java")).thenReturn(false);

        mockMvc.perform(post("/api/course/create/Java"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCourse() throws Exception {
        Course course = new Course(1L, "Java", "Description", Arrays.asList("Step1"));
        when(courseService.getCourse(1L)).thenReturn(course);

        mockMvc.perform(get("/api/course/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"));
    }


    @Test
    void testGetAllCourses() throws Exception {
        Course course1 = new Course(1L, "Java", "Description1", Arrays.asList("Step1"));
        Course course2 = new Course(2L, "Python", "Description2", Arrays.asList("Step1", "Step2"));
        List<Course> courses = Arrays.asList(course1, course2);

        when(courseService.getCourses()).thenReturn(courses);

        mockMvc.perform(get("/api/course/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[1].name").value("Python"));
    }

    @Test
    void testDeleteCourse() throws Exception {
        when(courseService.deleteCourse(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/course/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCourseBadRequest() throws Exception {
        when(courseService.deleteCourse(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/course/delete/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCourseDescription() throws Exception {
        when(courseService.updateCourseDescription(1L, "Updated Description")).thenReturn(true);

        mockMvc.perform(put("/api/course/update/description/1/Updated Description"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCourseDescriptionBadRequest() throws Exception {
        when(courseService.updateCourseDescription(1L, "Updated Description")).thenReturn(false);

        mockMvc.perform(put("/api/course/update/description/1/Updated Description"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateCourseSteps() throws Exception {
        when(courseService.updateCourseSteps(1L, Arrays.asList("Step1", "Step2"))).thenReturn(true);

        mockMvc.perform(put("/api/course/update/steps/1/Step1,Step2"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCourseStepsBadRequest() throws Exception {
        when(courseService.updateCourseSteps(1L, Arrays.asList("Step1", "Step2"))).thenReturn(false);

        mockMvc.perform(put("/api/course/update/steps/1/Step1,Step2"))
                .andExpect(status().isBadRequest());
    }

}
