package com.example.studentsystem.Configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic studentTopic() {
        return new NewTopic("student_topic", 3, (short) 1);
    }

    @Bean
    public NewTopic courseTopic() {
        return new NewTopic("course_topic", 3, (short) 1);
    }
}

