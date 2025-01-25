package com.example.studentsystem.Controller;

import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Entity.Student;
import com.example.studentsystem.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/student")
public class StudentController {

    private final StudentService studentService;
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping(path = "/create/{studentName}") // http://localhost:8080/api/student/create/John
    public ResponseEntity<Student> createStudent(@PathVariable String studentName) {
        if (studentService.createStudent(studentName)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(path = "/get/{StudentId}") // http://localhost:8080/api/student/get/1
    public ResponseEntity<Student> getStudent(@PathVariable Long StudentId) {
        Student student = studentService.getStudent(StudentId);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(path = "/get/all") // http://localhost:8080/api/student/get/all
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getStudents();
        return ResponseEntity.ok(students);
    }

    @PutMapping(path = "/update/{studentId}/{description}") // http://localhost:8080/api/student/update/1/John
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @PathVariable String description) {
        if (studentService.updateStudent(studentId, description)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(path = "/delete/{studentId}")  // http://localhost:8080/api/student/delete/1
    public ResponseEntity<Student> deleteStudent(@PathVariable Long studentId) {
        if (studentService.deleteStudent(studentId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(path = "/get/courses/{studentId}") // http://localhost:8080/api/student/get/courses/1
    public ResponseEntity<List<Course>> getCourses(@PathVariable Long studentId) {
        List<Course> courses = studentService.getCourses(studentId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping(path = "/addCourse/{studentId}/{courseId}") // http://localhost:8080/api/student/addCourse/1/1
    public ResponseEntity<Student> addCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        if (studentService.addCourse(studentId, courseId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/removeCourse/{studentId}/{courseId}") // http://localhost:8080/api/student/removeCourse/1/1
    public ResponseEntity<Student> removeCourse(@PathVariable Long studentId, @PathVariable Long courseId) {
        if (studentService.removeCourse(studentId, courseId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
