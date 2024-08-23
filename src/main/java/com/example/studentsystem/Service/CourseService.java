package com.example.studentsystem.Service;

import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Repository.CourseRepository;
import com.example.studentsystem.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    // Attributes
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    // Constructor
    @Autowired
    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    // Methods
    public boolean createCourse (String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (isCourseExist(name)) {
            return false;
        }
        try {
            Course course = new Course();
            course.setName(name);
            course.setDescription("Course description");
            course.setSteps(new ArrayList<>());
            courseRepository.save(course);
            return true;

        } catch (Exception e) {
            System.err.println("Exception at creating course: " + e.getMessage());
            return false;
        }
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public boolean deleteCourse(Long id) {
        if (id == null) {
            return false;
        }
        try {
            studentRepository.findAll().forEach(student -> {
                List<Course> courses = student.getCourses();
                courses.removeIf(course -> course.getId().equals(id));
                student.setCourses(courses);
                studentRepository.save(student);
            });

            courseRepository.deleteById(id);
            return true;

        } catch (Exception e) {
            System.err.println("Exception at deleting course: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourseDescription(Long id, String description) {
        if (description == null || description.trim().isEmpty()) {
            return false;
        }
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isEmpty()) {
                return false;
            }
            Course course = optionalCourse.get();
            course.setDescription(description);
            courseRepository.save(course);
            return true;

        } catch (Exception e) {
            System.err.println("Exception at updating course: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCourseSteps(Long id, List<String> steps) {
        if (steps == null || steps.isEmpty()) {
            return false;
        }
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isEmpty()) {
                return false;
            }
            Course course = optionalCourse.get();
            course.setSteps(steps);
            courseRepository.save(course);
            return true;

        }
        catch (Exception e) {
            System.err.println("Exception at updating course steps: " + e.getMessage());
            return false;
        }
    }

    public boolean isCourseExist(String name) {
        try {
            List<Course> courses = courseRepository.findAll();
            if (courses != null && !courses.isEmpty()) {
                return courses.stream().anyMatch(course -> course.getName().equals(name));
            }

            return false;
        } catch (Exception e) {
            System.err.println("Exception at checking course existence: " + e.getMessage());
            return false;
        }
    }

}
