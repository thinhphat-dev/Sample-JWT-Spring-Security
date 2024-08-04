package com.poly.backend.config;

import com.poly.backend.service.UserService;
import com.poly.backend.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
/**
 * Lớp CustomUserDetail thực hiện chức năng cung cấp thông tin chi tiết người dùng cho Spring Security.
 * Nó lấy thông tin người dùng từ UserService và xây dựng đối tượng UserDetails.
 */
public class CustomUserDetail implements UserDetailsService {

    @Autowired
    UserService userService;

    /**
     * Phương thức tải thông tin người dùng bằng tên đăng nhập (login).
     *
     * @param username Tên đăng nhập của người dùng.
     * @return Đối tượng UserDetails chứa thông tin người dùng và quyền hạn.
     * @throws UsernameNotFoundException Nếu không tìm thấy người dùng hoặc thông tin không hợp lệ.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDTO userAccount = userService.findByUsernameLogin(username);
        if (userAccount == null || userAccount.getRole() == null || userAccount.getPassword() == null) {
            throw new UsernameNotFoundException("User not found or invalid credentials");

        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + (userAccount.getRole().equalsIgnoreCase("admin") ? "ADMIN" : "USER")));

        return new User(userAccount.getRole(), userAccount.getPassword(), authorities);
    }
}
