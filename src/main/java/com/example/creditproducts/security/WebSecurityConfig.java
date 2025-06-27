package com.example.creditproducts.security;

import com.example.creditproducts.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private  JwtAuthFilter jwtAuthFilter;

    public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {



        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry->
                        registry
                                .requestMatchers(HttpMethod.POST, "/auth/register", "/auth/login").permitAll()

                                // Разрешаем доступ к GET запросам для получения информации о кредитных продуктах (для всех)
                                .requestMatchers(HttpMethod.GET, "/", "/credit-products", "/credit-products/{id}").permitAll()
                                // Защищаем GET запрос получения данных клиента (доступно аутентифицированным пользователям)
                                .requestMatchers(HttpMethod.GET, "/clients/{id}").authenticated()
                                // Защищаем GET запрос получения данных клиента (доступно аутентифицированным пользователям)
                                .requestMatchers(HttpMethod.GET, "/applications/client/{clientId}").authenticated()

                                // Защищаем POST запросы для подачи заявки на кредит (доступно аутентифицированным пользователям)
                                .requestMatchers(HttpMethod.POST, "/applications").authenticated()

                                // Защищаем GET запрос для просмотра статуса заявки (доступно аутентифицированным пользователям)
                                .requestMatchers(HttpMethod.GET, "/applications/{id}").authenticated()


                                // Защищаем эндпоинты, требующие роли ADMIN
                                .requestMatchers(HttpMethod.POST, "/credit-products").hasRole("ADMIN") // Создание продукта
                                .requestMatchers(HttpMethod.PUT, "/credit-products/{id}").hasRole("ADMIN") // Обновление продукта
                                .requestMatchers(HttpMethod.PUT, "/applications/{id}/status").hasAnyRole("OPERATOR", "ADMIN")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)// Не используем сессии
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);// Сила шифрования
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }



}