package com.hoenn.pokecenter.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hoenn.pokecenter.dto.request.AuthenticatedUser;
import com.hoenn.pokecenter.entity.NurseJoy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class JwtService {

    @Value("${pokecenter-hoenn.security.jwt.secret-key}")
    private String secretKey;

    public String generateToken(NurseJoy nurseJoy){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        return JWT.create()
                .withSubject(nurseJoy.getNurseJoyId())
                .withClaim("email", nurseJoy.getEmail())
                .withClaim("name", nurseJoy.getName())
                .withClaim("role", nurseJoy.getRole().name())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .withIssuer("Hoenn Pokécenter")
                .sign(algorithm);
    }

    public Optional<AuthenticatedUser> validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("Hoenn Pokécenter")
                    .build()
                    .verify(token);

            return Optional.of(new AuthenticatedUser(
                    jwt.getSubject(),
                    jwt.getClaim("name").asString(),
                    jwt.getClaim("email").asString(),
                    jwt.getClaim("role").asString()
            ));

        } catch (JWTVerificationException exception){
            return Optional.empty();
        }
    }

}
