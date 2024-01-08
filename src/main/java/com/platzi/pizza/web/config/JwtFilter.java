package com.platzi.pizza.web.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. Validar que sea un Header Authorization válido
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si no hay header o está vacío o no empieza con "Bearer ", continuar con la
        // cadena de filtros
        if (authorizationHeader == null
                || authorizationHeader.isEmpty()
                || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Validar que el token (JWT) sea válido
        String token = authorizationHeader.replace("Bearer ", ""); // Remover el prefijo "Bearer "
        boolean isValid = jwtUtil.verify(token); // Verificar el token
        if (!isValid) { // Si no es válido, continuar con la cadena de filtros
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cargar el usuario del UserDetailsService
        String username = jwtUtil.getUsername(token); // Obtener el usuario del token
        // Cargar el usuario del UserDetailsService con un cast a User
        User user = (User) userDetailsService.loadUserByUsername(username);

        // 4. Cargar al usuario en el contexto de Spring Security
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(), // Nombre de usuario
                user.getPassword(), // Contraseña
                user.getAuthorities() // Roles
        );

        // Establecer la autenticación en el contexto de Spring Security
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication);
        filterChain.doFilter(request, response);
    }

}
