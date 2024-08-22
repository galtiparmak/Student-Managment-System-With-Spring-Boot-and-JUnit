package com.example.studentsystem.Controller;

import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/course")
public class CourseController {


    private final CourseService courseService;
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping(path = "/create/{courseName}") // http://localhost:8080/api/course/create/Java
    public ResponseEntity<Course> createCourse(@PathVariable String courseName) {
        if (courseService.createCourse(courseName)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(path = "/get/{courseId}") // http://localhost:8080/api/course/get/1
    public ResponseEntity<Course> getCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }

    @GetMapping(path = "/get/all") // http://localhost:8080/api/course/get/all
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getCourses();
        return ResponseEntity.ok(courses);
    }

    @DeleteMapping(path = "/delete/{courseId}")  // http://localhost:8080/api/course/delete/1
    public ResponseEntity<Course> deleteCourse(@PathVariable Long courseId) {
        if (courseService.deleteCourse(courseId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/update/description/{courseId}/{description}") // http://localhost:8080/api/course/update/description/1/Java
    public ResponseEntity<Course> updateCourseDescription(@PathVariable Long courseId, @PathVariable String description) {
        if (courseService.updateCourseDescription(courseId, description)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(path = "/update/steps/{courseId}/{steps}") // http://localhost:8080/api/course/update/steps/1/Java
    public ResponseEntity<Course> addStep(@PathVariable Long courseId, @PathVariable List<String> steps) {
        if (courseService.updateCourseSteps(courseId, steps)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
