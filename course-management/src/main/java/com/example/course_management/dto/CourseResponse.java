package com.example.course_management.dto;

import com.example.course_management.model.CourseStatus;

public class CourseResponse {
    private Long id;
    private String title;
    private CourseStatus status;
    private Long instructorId;

    // Constructor để map từ Entity sang DTO
    public CourseResponse(Long id, String title, CourseStatus status, Long instructorId) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.instructorId = instructorId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
    public Long getInstructorId() { return instructorId; }
    public void setInstructorId(Long instructorId) { this.instructorId = instructorId; }
}