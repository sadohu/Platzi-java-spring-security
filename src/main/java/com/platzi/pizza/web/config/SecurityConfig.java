package com.platzi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Deshabilitar CSRF:
        // httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity
                // Deshabilitar CSRF:
                .csrf().disable()
                // Deshabilitar CORS:
                .cors().and()

                // Autorizar todas las peticiones:
                .authorizeRequests()

                // Permitir peticiones GET a /api/* (Primer nivel) o /api/** (Cualquier
                // subruta):
                // .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pizzas/**").permitAll()

                // Denegar peticiones PUT a todo el proyecto:
                .requestMatchers(HttpMethod.PUT).denyAll()

                // Cualquier peticiones:
                .anyRequest()

                // Se permiten todas las peticiones: (No deseado en producción)
                // .permitAll();

                // Se requiere autenticación: (Recomendado en producción)
                .authenticated()

                .and()

                // Se requiere autenticación básica:
                .httpBasic();
        return httpSecurity.build();
    }
}
