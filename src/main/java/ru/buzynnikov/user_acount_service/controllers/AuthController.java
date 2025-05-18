package ru.buzynnikov.user_acount_service.controllers;

import org.springframework.web.bind.annotation.*;
import ru.buzynnikov.user_acount_service.dto.JwtAuthResponse;
import ru.buzynnikov.user_acount_service.dto.SignInRequest;
import ru.buzynnikov.user_acount_service.security.AuthService;



@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth/sign-in")
    public JwtAuthResponse signIn(@RequestBody SignInRequest signInRequest) {
        System.out.println("signIn");
        return authService.signIn(signInRequest);
    }


}
