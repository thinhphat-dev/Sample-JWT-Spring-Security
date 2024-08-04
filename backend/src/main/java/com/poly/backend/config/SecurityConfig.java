package com.poly.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity

/**
 * Lớp SecurityConfig cung cấp cấu hình bảo mật cho ứng dụng.
 * Nó cấu hình các quy tắc bảo mật, các URL được bảo vệ và xác thực JWT token.
 */
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    @Autowired
    CustomJwtFilter jwtAuthFilter;
    private final String[] PROTECTED_URLS = {"/api/teachers/**"};

    /**
     * Phương thức cấu hình chuỗi lọc bảo mật
     *
     * @param http Đối tượng HttpSecurity
     * @return Đối tượng SecurityFilterChain
     * @throws Exception Ngoại lệ có thể xảy ra
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> {
                    /**
                     - requestMatchers...hasRole("ADMIN"): tức là các phương thức HTTP
                     này cần có quyền ADMIN để truy cập
                    -  PROTECTED_URLS: Danh sách các endpoint được bảo vệ yêu cầu quyền ADMIN để truy cập
                     */
                    auth.requestMatchers(HttpMethod.DELETE, PROTECTED_URLS).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, PROTECTED_URLS).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, PROTECTED_URLS).hasRole("ADMIN")
                            /**
                             - requestMatchers("/**").permitAll(): các phương thức với endpoint khác
                             được bắt đầu bằng dạng "/**" đều được truy cập
                             */
                            .requestMatchers("/**").permitAll()
                            /**
                             * - anyRequest().authenticated(): tất cả các request khác cần phải được xác thực
                             */
                            .anyRequest().authenticated();
                })
        ;
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

