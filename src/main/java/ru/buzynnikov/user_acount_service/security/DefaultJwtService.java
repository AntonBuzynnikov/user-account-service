package ru.buzynnikov.user_acount_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Function;


@Service
public class DefaultJwtService implements JwtService{

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Long extractId(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    @Override
    public String generateToken(UserAuthDTO dto) {
        Map<String, Object> claims = Map.of("id", dto.getUserId());
        return generateToken(claims, dto);
    }

    @Override
    public Boolean validateToken(String token, Long userId) {
        final Long id = extractId(token);
        return userId.equals(id);
    }


    private String generateToken(Map<String, Object> claims, UserAuthDTO dto) {
        return Jwts.builder()
                .claims(claims)
                .subject(dto.getEmail())
                .signWith(getJwtSecret())
                .compact();
    }

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }



    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getJwtSecret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getJwtSecret() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return  Keys.hmacShaKeyFor(keyBytes);
    }
}
