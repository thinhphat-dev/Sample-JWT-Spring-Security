package com.poly.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.backend.dto.ErrorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component

/**
 * Lớp UserAuthenticationEntryPoint thực hiện chức năng xử lý các yêu cầu không được xác thực.
 * Khi một yêu cầu không xác thực truy cập vào các URL yêu cầu xác thực, lớp này sẽ trả về phản hồi lỗi 401 (Unauthorized).
 */
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Phương thức commence thực hiện xử lý các yêu cầu không xác thực.
     *
     * @param request       Yêu cầu HTTP
     * @param response      Phản hồi HTTP
     * @param authException Ngoại lệ xác thực
     * @throws IOException      Ngoại lệ vào/ra
     * @throws ServletException Ngoại lệ Servlet
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), new ErrorDTO("Unauthorized path"));
    }
}
