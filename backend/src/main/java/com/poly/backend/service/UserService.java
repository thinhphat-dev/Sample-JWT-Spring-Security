package com.poly.backend.service;

import com.poly.backend.dto.CredentialsDTO;
import com.poly.backend.dto.SignUpDTO;
import com.poly.backend.dto.UserDTO;
import com.poly.backend.exception.AppException;
import com.poly.backend.repository.UserRepository;
import com.poly.backend.entity.User;
import com.poly.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;


public interface UserService {
    UserDTO login(CredentialsDTO credentialsDto);
    UserDTO register(SignUpDTO userDto);
    UserDTO findByUsernameLogin(String username);
}
