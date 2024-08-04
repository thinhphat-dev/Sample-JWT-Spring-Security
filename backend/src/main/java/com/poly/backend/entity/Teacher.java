package com.poly.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Setter
@Getter
@Table(name = "teacher")
public class Teacher extends AbstractEntity<Long> {

    @Column(name = "ma_giang_vien")
    private String maGiangVien;

    @Column(name = "email_ca_nhan")
    private String emailCaNhan;

    @Column(name = "email_edu")
    private String emailEdu;

    @Column(name = "hinh_dai_dien")
    private String hinhDaiDien;

    @Column(name = "ho_giang_vien")
    private String hoGiangVien;

    @Column(name = "ngay_sinh")
    @Temporal(TemporalType.DATE)
    private Date ngaySinh;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "ten_giang_vien")
    private String tenGiangVien;

}
