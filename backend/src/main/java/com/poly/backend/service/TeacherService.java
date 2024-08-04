package com.poly.backend.service;

import com.poly.backend.dto.TeacherDTO;
import com.poly.backend.entity.Teacher;
import com.poly.backend.mapper.TeacherMapper;
import com.poly.backend.repository.TeacherRepository;
import com.poly.backend.service.impl.TeacherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface TeacherService   {
    TeacherDTO getTeacherById(Long id);


    List<TeacherDTO> getAllTeachers();

    void addTeacher(TeacherDTO teacherDTO);

    void updateTeacher(long id, TeacherDTO teacherDTO);

    void deleteTeacher(long id);

}
