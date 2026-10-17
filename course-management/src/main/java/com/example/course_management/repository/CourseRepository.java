package com.example.course_management.repository;

import com.example.course_management.dto.CourseResponseV2;
import com.example.course_management.model.Course;
import com.example.course_management.model.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // V1: Lọc theo status và phân trang (Trả về Entity - dễ dính N+1)
    @Query("SELECT c FROM Course c WHERE c.status = :status")
    Page<Course> findAllByStatus(@Param("status") CourseStatus status, Pageable pageable);

    // V2: Class-based Projection để map thẳng vào DTO (Tối ưu hiệu năng, giải quyết N+1)
    @Query("""
           SELECT new com.example.course_management.dto.CourseResponseV2(
               c.id, 
               c.title, 
               c.status
           )
           FROM Course c
           WHERE c.status = :status
           """)
    Page<CourseResponseV2> findAllByStatusV2(@Param("status") CourseStatus status, Pageable pageable);
}