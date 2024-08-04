package com.poly.backend.controller;

import com.poly.backend.dto.TeacherDTO;
import com.poly.backend.exception.AppException;
import com.poly.backend.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);


    /**
     * Endpoint: GET /api/teachers
     * Chức năng: Lấy danh sách tất cả Giảng viên.
     * Xử lý: Gọi teacherService.getAllTeachers() để lấy danh sách Giảng viên từ dịch vụ.
     * Phản hồi: Trả về danh sách Giảng viên và thông điệp thành công nếu không có lỗi, hoặc phản hồi lỗi nếu có lỗi.
     */
    @GetMapping
    public List<TeacherDTO> getAllTeachers() {
        try {
            return teacherService.getAllTeachers();
        } catch (AppException e) {
            logger.error("Lỗi khi lấy toàn bộ danh sách Giảng viên: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Endpoint: GET /api/teachers/{id}
     * Chức năng: Lấy thông tin của một Giảng viên theo ID.
     * Xử lý: Gọi teacherService.getTeacherById(id) để lấy thông tin của Giảng viên.
     * Phản hồi: Trả về thông tin của Giảng viên nếu tồn tại, hoặc phản hồi lỗi nếu không tìm thấy Giảng viên.
     */
    @GetMapping("/{id}")
    public TeacherDTO getTeacherById(@PathVariable long id) {
        try {
            return teacherService.getTeacherById(id);
        } catch (AppException e) {
            logger.error("Lỗi khi lấy thông tin Giảng viên, ID: {}: {}", id, e.getMessage());
            return null;
        }

    }

    /**
     * Endpoint: POST /api/teachers
     * Chức năng: Thêm một Giảng viên mới.
     * Xử lý: Gọi teacherService.addTeacher(teacherDTO) để thêm Giảng viên mới.
     * Phản hồi: Trả về thông tin của Giảng viên đã được thêm và thông báo thành công, hoặc phản hồi lỗi nếu có lỗi.
     */
    @PostMapping
    public ResponseEntity<String> addTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        try {
            teacherService.addTeacher(teacherDTO);
            logger.info("Thêm Giảng viên thành công");
            return ResponseEntity.status(HttpStatus.CREATED).body("Thêm giảng viên thành công");
        } catch (AppException ex) {
            logger.error("Lỗi khi thêm Giảng viên: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * Endpoint: PUT /api/teachers/{id}
     * Chức năng: Cập nhật thông tin của một Giảng viên.
     * Xử lý: Gọi teacherService.updateTeacher(id, teacherDTO) để cập nhật thông tin Giảng viên.
     * Phản hồi: Trả về thông tin của Giảng viên sau khi cập nhật và thông báo thành công, hoặc phản hồi lỗi nếu không tìm thấy Giảng viên để cập nhật.
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateTeacher(@PathVariable long id, @Valid @RequestBody TeacherDTO teacherDTO) {
        try {
            teacherService.updateTeacher(id, teacherDTO);
            logger.info("Cập nhật thông tin Giảng viên thành công, ID: {}", id);
            return ResponseEntity.ok("Cập nhật thông tin giảng viên thành công");
        } catch (AppException ex) {
            logger.error("Lỗi khi cập nhật thông tin Giảng viên, ID: {}: {}", id, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * Endpoint: DELETE /api/teachers/{id}
     * Chức năng: Xóa một Giảng viên.
     * Xử lý: Gọi teacherService.deleteTeacher(id) để xóa Giảng viên.
     * Phản hồi: Trả về thông báo thành công nếu xóa thành công, hoặc phản hồi lỗi nếu không tìm thấy Giảng viên để xóa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTeacher(@PathVariable long id) {
        try {
            teacherService.deleteTeacher(id);
            logger.info("Xóa Giảng viên thành công, ID: {}", id);
            return ResponseEntity.ok("Xóa thông tin giảng viên thành công");
        } catch (AppException ex) {
            logger.error("Lỗi khi xóa Giảng viên, ID: {}: {}", id, ex.getMessage());
            return null;
        }

    }
}
