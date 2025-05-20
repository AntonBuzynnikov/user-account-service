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


/**
 * Реализация сервисов для работы с JWT-токенами.
 * Предоставляет методы для генерации, проверки и декодирования токенов.
 */
@Service
public class DefaultJwtService implements JwtService{

    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Извлекает идентификатор пользователя из JWT-токена.
     *
     * @param token JWT-токен
     * @return идентификатор пользователя
     */
    @Override
    public Long extractId(String token) {
        return extractClaim(token, claims -> claims.get("id", Long.class));
    }

    /**
     * Генерирует JWT-токен для указанного пользователя.
     *
     * @param dto объект с информацией о пользователе
     * @return строка с JWT-токеном
     */
    @Override
    public String generateToken(UserAuthDTO dto) {
        Map<String, Object> claims = Map.of("id", dto.getUserId());
        return generateToken(claims, dto);
    }

    /**
     * Проверяет валидность токена по сравнению с идентификатором пользователя.
     *
     * @param token   JWT-токен
     * @param userId  идентификатор пользователя
     * @return true, если токен действителен, false в противном случае
     */
    @Override
    public Boolean validateToken(String token, Long userId) {
        final Long id = extractId(token);
        return userId.equals(id);
    }


    /**
     * Генерация JWT-токена с указанием субъекта и дополнительной информации.
     *
     * @param claims map с дополнительными данными
     * @param dto    объект с информацией о пользователе
     * @return строка с JWT-токеном
     */
    private String generateToken(Map<String, Object> claims, UserAuthDTO dto) {
        return Jwts.builder()
                .claims(claims)
                .subject(dto.getEmail())
                .signWith(getJwtSecret())
                .compact();
    }

    /**
     * Извлекает email пользователя из JWT-токена.
     *
     * @param token JWT-токен
     * @return email пользователя
     */
    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Универсальная функция для извлечения произвольных утверждений (claim) из JWT-токена.
     *
     * @param token           JWT-токен
     * @param claimsResolver  функция для извлечения утверждения
     * @param <T>             тип ожидаемого утверждения
     * @return утверждение из токена
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Декодирует весь набор утверждений (claims) из JWT-токена.
     *
     * @param token JWT-токен
     * @return объект Claims с набором утверждений
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getJwtSecret())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Возвращает секретный ключ для подписания и проверки JWT-токенов.
     *
     * @return секретный ключ
     */
    private SecretKey getJwtSecret() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return  Keys.hmacShaKeyFor(keyBytes);
    }
}
