package com.example.course_management.dto;

import com.example.course_management.model.CourseStatus;

public class CourseResponseV2 {
    private Long id;
    private String title;
    private CourseStatus status;

    // Bắt buộc phải có Constructor này cho JPQL Projection
    public CourseResponseV2(Long id, String title, CourseStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
}