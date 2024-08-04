package com.poly.backend.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.*;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
/**
 * Lớp CustomJwtFilter thực hiện chức năng lọc và xác thực JWT token cho mỗi yêu cầu HTTP.
 * Nó kiểm tra JWT token trong header của yêu cầu, xác thực token và thiết lập thông tin người dùng trong SecurityContext.
 */
public class CustomJwtFilter extends OncePerRequestFilter {

    @Value("${jwt.privateKey}")
    private String secretKey;

    @Autowired
    private CustomUserDetail userDetailsService;
    /**
     * Phương thức lọc từng yêu cầu và xác thực JWT token
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFormReq(request);
        if (token != null) {
            if (verifyToken(token)) {
                String data = parserToken(token);
                if (data != null && !data.isEmpty()) {
                   String username = extractUsername(data);
                    UserDetails userDetails = loadUserByUsername(username);
                    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
    /**
     * Phương thức phân tích JWT token và trích xuất tên người dùng
     * @param token
     * @return tên người dùng được trích xuất từ token
     */
    public String parserToken(String token) {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
    /**
     * Phương thức lấy JWT token từ yêu cầu
     * @param request
     * @return token được lấy từ yêu cầu
     */
    public String getTokenFormReq(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }
        return token;
    }
    /**
     * Phương thức xác thực JWT token
     * @param token
     * @return true nếu token hợp lệ, ngược lại false
     */
    public boolean verifyToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Phương thức tải thông tin người dùng từ tên người dùng
     * @param username
     * @return thông tin người dùng
     */
    private UserDetails loadUserByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String data) {
        Pattern pattern = Pattern.compile("username=([^,]*)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

}



