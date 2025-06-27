package com.example.creditproducts.jwt;

import com.example.creditproducts.security.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // 1. Получаем заголовок Authorization
        final String authHeader = request.getHeader("Authorization");
        // 2. Проверяем наличие и формат заголовка
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 3. Извлекаем токен (после "Bearer ")
        final String jwt = authHeader.substring(7);
        // 4. Проверяем токен
        if (jwtUtils.validateToken(jwt)) {
            // 5. Получаем имя пользователя
            String username = jwtUtils.getUsernameFromToken(jwt);
            // 6. Загружаем пользователя из БД
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // 7. Создаём объект аутентификации
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            // 8. Сохраняем в SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 9. Передаём запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}