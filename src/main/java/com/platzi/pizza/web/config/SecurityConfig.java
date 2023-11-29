package com.platzi.pizza.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Autorizar todas las peticiones:
                .authorizeRequests()

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
