package com.poly.backend.config;

import com.poly.backend.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
@Component
/**
 * Lớp UserAuthenticationProvider cung cấp chức năng tạo JWT token cho người dùng.
 */
public class UserAuthenticationProvider {

    @Value("${jwt.privateKey}")
    private String secretKey;
    @Value("${jwt.expirationMs}")
    private long expirationMs;

    /**
     * Phương thức tạo JWT token với thông tin người dùng
     *
     * @param data Thông tin người dùng (subject)
     * @return JWT token dưới dạng chuỗi
     */
    public String createToken(String data) {
        // Giải mã và lấy khóa bí mật từ chuỗi secretKey
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        // Tạo JWT token
        return Jwts.builder()
                .setSubject(data)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Phương thức tạo payload cho JWT token với thông tin từ UserDTO
     *
     * @param userDto Thông tin người dùng dưới dạng UserDTO
     * @return Chuỗi payload chứa thông tin người dùng
     */
    public String createTokenPayload(UserDTO userDto) {
        // Tạo payload từ các thuộc tính của UserDTO
        return "UserDTO(id=" + userDto.getId() +
                ", firstName=" + userDto.getFirstName() +
                ", lastName=" + userDto.getLastName() +
                ", username=" + userDto.getUsername() +
                ", role=" + userDto.getRole();
    }
}
