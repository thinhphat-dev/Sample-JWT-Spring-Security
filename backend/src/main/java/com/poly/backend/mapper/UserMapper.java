package com.poly.backend.mapper;

import com.poly.backend.dto.SignUpDTO;
import com.poly.backend.dto.UserDTO;
import com.poly.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDTO signUpDto);

}
