package com.poly.backend.controller;

import com.poly.backend.config.UserAuthenticationProvider;
import com.poly.backend.dto.CredentialsDTO;
import com.poly.backend.dto.SignUpDTO;
import com.poly.backend.dto.UserDTO;
import com.poly.backend.exception.AppException;
import com.poly.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
/**
 * Chức năng: Controller xử lý yêu cầu
 * đăng nhập hoặc đăng ký từ người dùng.
 */
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     * Endpoint: POST /login
     * Chức năng: Đăng nhập vào hệ thống.
     * Xử lý: Gọi userService.login(credentialsDto) để xác thực người dùng.
     * Phản hồi: Trả về thông tin người dùng và token nếu xác thực thành công, hoặc phản hồi lỗi nếu không thành công.
     */
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody @Valid CredentialsDTO credentialsDto) {

        try {
            UserDTO userDto = userService.login(credentialsDto);
            userDto.setToken(userAuthenticationProvider.createToken(userAuthenticationProvider.createTokenPayload(userDto)));
            logger.info("Đăng nhập thành công cho người dùng: {}", userDto.getRole());
            return ResponseEntity.ok(userDto);
        } catch (AppException e) {
            logger.error("Lỗi khi đăng nhập: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }

    /**
     * Endpoint: POST /register
     * Chức năng: Đăng ký người dùng mới.
     * Xử lý: Gọi userService.register(user) để tạo mới người dùng.
     * Phản hồi: Trả về thông tin người dùng đã được tạo và token, hoặc phản hồi lỗi nếu không thành công.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid SignUpDTO user) {
        try {
            UserDTO createdUser = userService.register(user);
            createdUser.setToken(userAuthenticationProvider.createToken(userAuthenticationProvider.createTokenPayload(createdUser)));
            logger.info("Đăng ký thành công cho người dùng: {}", createdUser.getRole());
            return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(null);
        } catch (AppException e) {
            logger.error("Lỗi khi đăng ký: {}", e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }
}
