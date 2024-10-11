package com.example.studentsystem.ControllerTest;

import com.example.studentsystem.Controller.StudentController;
import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Entity.Student;
import com.example.studentsystem.Service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    // Write your tests here
    @Test
    void testCreateStudent() throws Exception {
        when(studentService.createStudent("John")).thenReturn(true);
        mockMvc.perform(post("/api/student/create/John"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateStudentBadRequest() throws Exception {
        when(studentService.createStudent("John")).thenReturn(false);
        mockMvc.perform(post("/api/student/create/John"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStudent() throws Exception {
        Student student = new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1"))));
        when(studentService.getStudent(1L)).thenReturn(student);

        mockMvc.perform(get("/api/student/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    void testGetStudentBadRequest() throws Exception {
        when(studentService.getStudent(1L)).thenReturn(null);
        mockMvc.perform(get("/api/student/get/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllStudents() throws Exception {
        Student student1 = new Student(1L, "John", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step1"))));
        Student student2 = new Student(2L, "Jane", "Student", Arrays.asList(new Course(1L, "Java", "Description", Arrays.asList("Step2"))));
        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getStudents()).thenReturn(students);

        mockMvc.perform(get("/api/student/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[1].name").value("Jane"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        when(studentService.updateStudent(1L, "John")).thenReturn(true);
        mockMvc.perform(put("/api/student/update/1/John"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateStudentBadRequest() throws Exception {
        when(studentService.updateStudent(1L, "John")).thenReturn(false);
        mockMvc.perform(put("/api/student/update/1/John"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteStudent() throws Exception {
        when(studentService.deleteStudent(1L)).thenReturn(true);
        mockMvc.perform(delete("/api/student/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteStudentBadRequest() throws Exception {
        when(studentService.deleteStudent(1L)).thenReturn(false);
        mockMvc.perform(delete("/api/student/delete/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCourse() throws Exception {
        when(studentService.addCourse(1L, 1L)).thenReturn(true);
        mockMvc.perform(put("/api/student/addCourse/1/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddCourseBadRequest() throws Exception {
        when(studentService.addCourse(1L, 1L)).thenReturn(false);
        mockMvc.perform(put("/api/student/addCourse/1/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRemoveCourse() throws Exception {
        when(studentService.removeCourse(1L, 1L)).thenReturn(true);
        mockMvc.perform(put("/api/student/removeCourse/1/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveCourseBadRequest() throws Exception {
        when(studentService.removeCourse(1L, 1L)).thenReturn(false);
        mockMvc.perform(put("/api/student/removeCourse/1/1"))
                .andExpect(status().isBadRequest());
    }
}
