package com.example.course_management.service;

import com.example.course_management.dto.CourseCreateRequest;
import com.example.course_management.dto.CourseResponse;
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

    // Hàm phân trang theo yêu cầu bài toán
    public Page<CourseResponse> getPagedCourses(int page, int size, String sortBy, Sort.Direction direction) {
        // Kiểm tra an toàn cho tham số page
        if (page < 0) {
            page = 0;
        }

        // Logic sắp xếp: nếu sortBy bị null hoặc rỗng thì mặc định xếp theo "id"
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "id";
        }

        // Tạo đối tượng Sort
        Sort sort = Sort.by(direction, sortBy);

        // Tạo đối tượng Pageable
        Pageable pageable = PageRequest.of(page, size, sort);

        // Lấy dữ liệu từ database dạng Page<Course>
        Page<Course> coursePage = courseRepository.findAll(pageable);

        // Map Page<Course> sang Page<CourseResponse>
        return coursePage.map(course -> new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getStatus(),
                course.getInstructor() != null ? course.getInstructor().getId() : null
        ));
    }
}