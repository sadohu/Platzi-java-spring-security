package com.platzi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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

    @Bean
    public UserDetailsService memoryUsers() {
        /**
         * Crear un usuario en memoria, con sus propias credenciales
         * De esta manera sustituimos el usuario por defecto de Spring Security
         * Por ultimo, Sring Security no generará una contraseña aleatoria.
         */

        UserDetails admin = User.builder()
                .username("admin")
                /**
                 * .password("admin")
                 * No se recomienda guardar la contraseña en texto plano
                 * Por lo que se debe encriptar
                 */

                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
