package com.platzi.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(securedEnabled = true)
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

                // Permitir peticiones a /api/auth/** para el login
                .requestMatchers("/api/auth/**").permitAll()

                // Permitir peticiones GET a /api/* (Primer nivel) o /api/** (Cualquier
                // subruta):
                // .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                // .requestMatchers(HttpMethod.GET, "/api/pizzas/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("ADMIN", "CUSTOMER")

                /*
                 * ROLES: Establecer reglas para cada rol
                 * Para este caso establecemos que solo el Administrador puede solicitar
                 * peticiones POST
                 * y editamos las peticiones GET para ciertos roles: (lineas arriba)
                 * Reemplazamos:
                 * .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                 * Por:
                 * .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("ADMIN",
                 * "CUSTOMER")
                 *
                 * Perzonalizaremos de la misma manera los métodos POST y PUT
                 * u otros paths que requieran de un rol especifico
                 */
                .requestMatchers(HttpMethod.POST, "/api/pizzas/**").hasRole("ADMIN")

                // Denegar peticiones PUT a todo el proyecto:
                // .requestMatchers(HttpMethod.PUT).denyAll()
                .requestMatchers(HttpMethod.PUT).hasRole("ADMIN")

                /*
                 * AUTHORITY
                 * Los authority son permisos que se pueden asignar a un usuario o grupo de
                 * usuarios,
                 * dependiendo de la Lógica en la implementación de UserDetailsService,
                 * Para el ejemplo: Todos los usuarios tienes el permiso de ver una orden
                 * aleatoria,
                 * Sin embargo, solo el administrador puede ver todos los enpoints de ordenes.
                 * Por ultimo, IMPORTANTE, los authority se deben definir antes que los roles,
                 * ya que el SecurityFilterChain se ejecuta en orden escalonada.
                 */
                .requestMatchers("/api/orders/random").hasAuthority("random_order")

                /*
                 * ROLES
                 * Para este caso establecemos que solo el Administrador
                 * puede solicitar peticiones (GET, POST, PUT, DELETE) a /api/orders/**
                 */
                .requestMatchers("/api/orders/**").hasRole("ADMIN")

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

    // @Bean
    // public UserDetailsService memoryUsers() {
    // /**
    // * Crear un usuario en memoria, con sus propias credenciales
    // * De esta manera sustituimos el usuario por defecto de Spring Security
    // * Por ultimo, Sring Security no generará una contraseña aleatoria.
    // */

    // UserDetails admin = User.builder()
    // .username("admin")
    // /**
    // * .password("admin")
    // * No se recomienda guardar la contraseña en texto plano
    // * Por lo que se debe encriptar
    // */

    // .password(passwordEncoder().encode("admin"))
    // .roles("ADMIN")
    // .build();

    // /*
    // * Para el manejo de roles, creamos un usuario de tipo CUSTOMER,
    // * y luego personalizaremos el filterChain para que cada rol tenga sus propias
    // * reglas
    // */
    // UserDetails customer = User.builder()
    // .username("customer")
    // .password(passwordEncoder().encode("customer123"))
    // .roles("CUSTOMER")
    // .build();

    // return new InMemoryUserDetailsManager(admin, customer);
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
