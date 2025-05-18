package ru.buzynnikov.user_acount_service.security;

import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;

public interface JwtService {

    Long extractId(String token);

    String generateToken(UserAuthDTO dto);

    Boolean validateToken(String token, Long userId);

    String extractEmail(String token);
}
