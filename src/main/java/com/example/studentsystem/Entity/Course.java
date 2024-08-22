package com.example.studentsystem.Entity;

import jakarta.persistence.*;

import java.util.List;
@Entity
@Table(name= "courses")
public class Course {
    @Column(name = "id", nullable = false, unique = true)
    @Id
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "steps")
    @ElementCollection
    private List<String> steps;

    public Course() {
    }

    public Course (Long id, String name, String description, List<String> steps) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setSteps(steps);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", steps=" + steps +
                '}';
    }
}

