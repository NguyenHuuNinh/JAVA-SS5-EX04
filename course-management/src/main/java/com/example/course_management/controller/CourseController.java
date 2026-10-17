package com.example.course_management.controller;

import com.example.course_management.dto.ApiResponse;
import com.example.course_management.dto.CourseCreateRequest;
import com.example.course_management.dto.CourseResponse;
import com.example.course_management.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    // Endpoint lấy danh sách khóa học có phân trang và sắp xếp
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CourseResponse>>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        // Gọi hàm từ Service
        Page<CourseResponse> resultPage = courseService.getPagedCourses(page, size, sortBy, direction);

        // Bọc kết quả vào ApiResponse chuẩn
        ApiResponse<Page<CourseResponse>> response = new ApiResponse<>("Ok", true, resultPage);

        return ResponseEntity.ok(response);
    }
}