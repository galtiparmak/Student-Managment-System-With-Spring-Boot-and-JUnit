package com.example.studentsystem.Service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "student_topic", groupId = "default-group")
    public void consumeStudentTopic(String message) {
        System.out.println("Received message from student_topic: " + message);
    }

    @KafkaListener(topics = "course_topic", groupId = "default-group")
    public void consumeCourseTopic(String message) {
        System.out.println("Received message from course_topic: " + message);
    }
}

