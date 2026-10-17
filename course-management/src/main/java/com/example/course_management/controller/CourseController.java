package com.example.course_management.controller;

import com.example.course_management.dto.ApiResponse;
import com.example.course_management.dto.CourseCreateRequest;
import com.example.course_management.dto.CourseResponse;
import com.example.course_management.dto.CourseResponseV2;
import com.example.course_management.dto.PageResponse;
import com.example.course_management.model.CourseStatus;
import com.example.course_management.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCourse(@RequestBody CourseCreateRequest req) {
        courseService.createCourse(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Course Created Successfully", true, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCourse(@PathVariable Long id, @RequestBody CourseCreateRequest req) {
        courseService.updateCourse(id, req);
        return ResponseEntity.ok(new ApiResponse<>("Course Updated Successfully", true, null));
    }

    // V1: Endpoint lấy danh sách khóa học (Cách cũ)
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponse>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "ACTIVE") CourseStatus status) {

        PageResponse<CourseResponse> resultPage = courseService.getPagedCoursesByStatus(page, size, sortBy, direction, status);

        ApiResponse<PageResponse<CourseResponse>> response = new ApiResponse<>("Ok", true, resultPage);
        return ResponseEntity.ok(response);
    }

    // V2: Endpoint lấy danh sách khóa học (Đã tối ưu DTO Projection)
    @GetMapping("/v2")
    public ResponseEntity<ApiResponse<PageResponse<CourseResponseV2>>> getAllCoursesV2(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "ACTIVE") CourseStatus status) {

        // Gọi hàm V2 từ Service
        PageResponse<CourseResponseV2> resultPage = courseService.getPagedCoursesByStatusV2(page, size, sortBy, direction, status);

        ApiResponse<PageResponse<CourseResponseV2>> response = new ApiResponse<>("Ok", true, resultPage);
        return ResponseEntity.ok(response);
    }
}