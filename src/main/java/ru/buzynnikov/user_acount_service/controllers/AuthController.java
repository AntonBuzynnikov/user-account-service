package ru.buzynnikov.user_acount_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.buzynnikov.user_acount_service.dto.JwtAuthResponse;
import ru.buzynnikov.user_acount_service.dto.SignInRequest;
import ru.buzynnikov.user_acount_service.security.AuthService;


/**
 * Контроллер для обработки процессов аутентификации и выдачи JWT-токенов пользователям.
 */
@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Обработчик POST-запроса для входа пользователя.
     *
     * @param signInRequest Объект запроса, содержащий данные для входа (логин и пароль).
     * @return Ответ с JWT-токеном для успешной аутентификации.
     */
    @PostMapping("/auth/sign-in")
    public ResponseEntity<JwtAuthResponse> signIn(@RequestBody SignInRequest signInRequest) {
        System.out.println("signIn");
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }


}
