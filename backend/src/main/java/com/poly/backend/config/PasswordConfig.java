package com.poly.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
/**
 * Lớp PasswordConfig cung cấp cấu hình cho mã hóa mật khẩu trong ứng dụng.
 * Nó sử dụng BCrypt để mã hóa mật khẩu.
 */
public class PasswordConfig {
    /**
     * Phương thức tạo một bean PasswordEncoder sử dụng BCryptPasswordEncoder.
     *
     * @return Đối tượng PasswordEncoder sử dụng BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
