package com.poly.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private String emailCaNhan;
    private String emailEdu;
    private String hinhDaiDien;
    private String hoGiangVien;
    private String maGiangVien;
    private Date ngaySinh;
    private String tenGiangVien;
    private String soDienThoai;

}
