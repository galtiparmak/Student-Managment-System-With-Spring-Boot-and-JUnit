package com.example.studentsystem.Service;

import com.example.studentsystem.Entity.Course;
import com.example.studentsystem.Entity.Student;
import com.example.studentsystem.Repository.CourseRepository;
import com.example.studentsystem.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final String STUDENT_TOPIC = "student_topic";

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public StudentService(StudentRepository studentRepository, CourseRepository courseRepository, KafkaProducer kafkaProducer) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.kafkaProducer = kafkaProducer;
    }

    public boolean createStudent(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        try {
            Student student = new Student();
            student.setName(name);
            student.setDescription("Student");
            student.setCourses(new ArrayList<>());
            studentRepository.save(student);
            kafkaProducer.sendMessage(STUDENT_TOPIC, "Student created: " + student.getName());

            return true;

        } catch (Exception e) {
            System.err.println("Exception at creating student: " + e.getMessage());
            return false;
        }
    }

    public Student getStudent(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return null;
        }

        Student student = optionalStudent.get();
        kafkaProducer.sendMessage(STUDENT_TOPIC, "Student retrieved: " + student.getName());

        return student;
    }


    public List<Student> getStudents() {
        List<Student> students = studentRepository.findAll();
        students.forEach(student-> {
            kafkaProducer.sendMessage(STUDENT_TOPIC, "Student retrieved: " + student.getName());
        });

        return studentRepository.findAll();
    }

    public boolean updateStudent(Long id, String description) {
        if (id == null || description == null || description.trim().isEmpty()) {
            return false;
        }
        try {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            if (optionalStudent.isEmpty()) {
                return false;
            }

            Student student = optionalStudent.get();
            student.setDescription(description);
            studentRepository.save(student);

            kafkaProducer.sendMessage(STUDENT_TOPIC, "Student updated: " + student.getName());

            return true;

        } catch (Exception e) {
            System.err.println("Exception at updating student: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteStudent(Long id) {
        if (id == null) {
            return false;
        }
        try {
            Optional<Student> optionalStudent = studentRepository.findById(id);
            if (optionalStudent.isEmpty()) {
                return false;
            }

            Student student = optionalStudent.get();
            studentRepository.delete(student);

            kafkaProducer.sendMessage(STUDENT_TOPIC, "Student deleted: " + student.getName());

            return true;

        } catch (Exception e) {
            System.err.println("Exception at deleting student: " + e.getMessage());
            return false;
        }
    }

    public boolean addCourse(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }
        try {
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if (optionalStudent.isEmpty()) {
                return false;
            }

            Student student = optionalStudent.get();
            Optional<Course> optionalCourse = courseRepository.findById(courseId);
            if (optionalCourse.isEmpty()) {
                return false;
            }

            if (isCourseEnrolled(studentId, courseId)) {
                return false;
            }

            Course course = optionalCourse.get();
            student.getCourses().add(course);
            studentRepository.save(student);

            kafkaProducer.sendMessage(STUDENT_TOPIC, "Course " + course.getName() + " added to student: " + student.getName());

            return true;

        } catch (Exception e) {
            System.err.println("Exception at adding course: " + e.getMessage());
            return false;
        }
    }

    public boolean removeCourse(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }

        try {
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if (optionalStudent.isEmpty()) {
                return false;
            }

            Student student = optionalStudent.get();

            List<Course> courses = student.getCourses();
            if (courses != null && !courses.isEmpty()) {
                boolean removed = courses.removeIf(course -> course.getId().equals(courseId));
                if (removed) {
                    studentRepository.save(student);
                    kafkaProducer.sendMessage(STUDENT_TOPIC, "Course removed from student: " + student.getName());
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.err.println("Exception at removing course: " + e.getMessage());
            return false;
        }
    }

    public boolean isCourseEnrolled(Long studentId, Long courseId) {
        if (studentId == null || courseId == null) {
            return false;
        }

        try {
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if (optionalStudent.isEmpty()) {
                return false;
            }

            Student student = optionalStudent.get();

            List<Course> courses = student.getCourses();
            if (courses != null && !courses.isEmpty()) {
                return courses.stream().anyMatch(course -> course.getId().equals(courseId));
            }

            return false;
        } catch (Exception e) {
            System.err.println("Exception at checking course enrollment: " + e.getMessage());
            return false;
        }
    }

}
