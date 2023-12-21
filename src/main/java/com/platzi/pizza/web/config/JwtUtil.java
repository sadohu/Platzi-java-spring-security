package com.platzi.pizza.web.config;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component // Indica que es un componente de Spring
public class JwtUtil {
    private final static String SECRET_KEY = "pl4tz1_p1zz4"; // Llave secreta para firmar el token
    private final static Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY); // Algoritmo para firmar el token

    // Método para crear el token
    public String create(String username) {
        return JWT.create() // Crea el token
                .withSubject(username) // El asunto será el usuario en cuestion
                .withIssuer("platzi-pizza") // Quien crea el token
                .withIssuedAt(new Date()) // Fecha de creación
                .withExpiresAt(new Date(System.currentTimeMillis() +
                        TimeUnit.DAYS.toMillis(15))) // Fecha de expiración (15 días)
                .sign(ALGORITHM); // Firma el token con el algoritmo creado en la parte superior
    }
}
