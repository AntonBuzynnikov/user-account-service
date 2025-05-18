package ru.buzynnikov.user_acount_service.security;

import ru.buzynnikov.user_acount_service.dto.JwtAuthResponse;
import ru.buzynnikov.user_acount_service.dto.SignInRequest;

public interface AuthService {
    JwtAuthResponse signIn(SignInRequest signInRequest);

}
