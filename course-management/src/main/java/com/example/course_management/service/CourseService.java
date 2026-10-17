package com.example.course_management.service;

import com.example.course_management.dto.CourseCreateRequest;
import com.example.course_management.dto.CourseResponse;
import com.example.course_management.dto.PageResponse;
import com.example.course_management.model.Course;
import com.example.course_management.model.Instructor;
import com.example.course_management.repository.CourseRepository;
import com.example.course_management.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public Course createCourse(CourseCreateRequest req) {
        Instructor instructor = instructorRepository.findById(req.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Instructor"));
        Course course = new Course(req.getTitle(), req.getStatus(), instructor);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseCreateRequest req) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Course"));
        Instructor instructor = instructorRepository.findById(req.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Instructor"));

        course.setTitle(req.getTitle());
        course.setStatus(req.getStatus());
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    // Hàm phân trang đã chuẩn hóa sang PageResponse
    public PageResponse<CourseResponse> getPagedCourses(int page, int size, String sortBy, Sort.Direction direction) {
        if (page < 0) {
            page = 0;
        }

        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "id";
        }

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy dữ liệu từ database (vẫn là Page của Spring)
        Page<Course> coursePage = courseRepository.findAll(pageable);

        // Chuyển đổi List<Course> sang List<CourseResponse>
        List<CourseResponse> courseResponses = coursePage.getContent().stream()
                .map(course -> new CourseResponse(
                        course.getId(),
                        course.getTitle(),
                        course.getStatus(),
                        course.getInstructor() != null ? course.getInstructor().getId() : null
                )).toList();

        // Bọc dữ liệu vào PageResponse tự định nghĩa
        return new PageResponse<>(
                courseResponses,
                coursePage.getNumber(),
                coursePage.getSize(),
                coursePage.getTotalElements(),
                coursePage.getTotalPages(),
                coursePage.isLast()
        );
    }
}