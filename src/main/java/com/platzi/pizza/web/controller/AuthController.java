package com.platzi.pizza.web.controller;

import com.platzi.pizza.service.dto.LoginDto;
import com.platzi.pizza.web.config.JwtUtil;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
// @AllArgsConstructor
public class AuthController {

    // private AuthenticationManager authenticationManager;
    // private JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        // Creamos un token a partir de los datos de loginDto
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                loginDto.getPassword());
        // Autenticamos el token - Si el token es correcto, se autentica, si no, se
        // lanza una excepción
        Authentication authentication = this.authenticationManager.authenticate(login);

        // Imprimimos en consola si el usuario está autenticado, si el usuario está
        // autenticado, se imprime el usuario
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());

        // Creamos el JWT
        String jwt = jwtUtil.create(loginDto.getUsername());

        // Retornamos el JWT en el header de la respuesta
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }

}
