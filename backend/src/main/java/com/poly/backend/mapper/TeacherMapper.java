package com.poly.backend.mapper;




import com.poly.backend.dto.TeacherDTO;
import com.poly.backend.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    TeacherDTO toDto(Teacher teacher);
    List<TeacherDTO> toDto(List<Teacher> teachers);
    Teacher toEntity(TeacherDTO teacherDTO);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(TeacherDTO teacherDTO, @MappingTarget Teacher teacher);
}
