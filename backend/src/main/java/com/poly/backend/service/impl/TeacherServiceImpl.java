package com.poly.backend.service.impl;

import com.poly.backend.dto.TeacherDTO;
import com.poly.backend.entity.Teacher;
import com.poly.backend.exception.AppException;
import com.poly.backend.mapper.TeacherMapper;
import com.poly.backend.repository.TeacherRepository;
import com.poly.backend.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private final TeacherRepository teacherRepository;
    @Autowired
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherDTO getTeacherById(Long id) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        return optionalTeacher.map(teacherMapper::toDto).orElse(null);
    }

    @Override
    public List<TeacherDTO> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        return teacherMapper.toDto(teachers);
    }

    @Override
    public void addTeacher(TeacherDTO teacherDTO) {
        validateTeacher(teacherDTO);
        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        teacherRepository.save(teacher);
    }

    @Override
    public void updateTeacher(long id, TeacherDTO teacherDTO) {
        validateTeacher(teacherDTO);
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            teacherMapper.updateFromDto(teacherDTO,teacher);
            teacherRepository.save(teacher);
        }
    }

    @Override
    public void deleteTeacher(long id) {
        teacherRepository.deleteById(id);
    }

    private void validateTeacher(TeacherDTO teacherDTO) {
        if (teacherDTO.getMaGiangVien() == null || teacherDTO.getMaGiangVien().isEmpty()) {
            throw new AppException("Mã giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getEmailCaNhan() == null || teacherDTO.getEmailCaNhan().isEmpty()) {
            throw new AppException("Email cá nhân không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getEmailEdu() == null || teacherDTO.getEmailEdu().isEmpty()) {
            throw new AppException("Email Edu không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getHinhDaiDien() == null || teacherDTO.getHinhDaiDien().isEmpty()) {
            throw new AppException("Hình đại diện không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getHoGiangVien() == null || teacherDTO.getHoGiangVien().isEmpty()) {
            throw new AppException("Họ giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }if (teacherDTO.getTenGiangVien() == null || teacherDTO.getTenGiangVien().isEmpty()) {
            throw new AppException("Tên giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }if (teacherDTO.getSoDienThoai() == null || teacherDTO.getSoDienThoai().isEmpty()) {
            throw new AppException("Số điện thoại giảng viên không được để trống", HttpStatus.BAD_REQUEST);
        }
        if (teacherDTO.getNgaySinh() == null) {
            throw new AppException("Ngày sinh không được để trống", HttpStatus.BAD_REQUEST);
        }

    }
}
