package ru.buzynnikov.user_acount_service.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.buzynnikov.user_acount_service.dto.JwtAuthResponse;
import ru.buzynnikov.user_acount_service.dto.SignInRequest;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;

@Service
public class DefaultAuthService implements AuthService{

    private final CustomUserDetailsService detailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public DefaultAuthService(CustomUserDetailsService detailsService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.detailsService = detailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtAuthResponse signIn(SignInRequest signInRequest) {
        System.out.println(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword()
        )));

        UserAuthDTO userAuthDTO = (UserAuthDTO) detailsService.loadUserByUsername(signInRequest.getEmail());
        String token;
        if (userAuthDTO != null) {
            token = jwtService.generateToken(userAuthDTO);
        } else {
            throw new RuntimeException("User not found");
        }


        return new JwtAuthResponse(token);
    }
}
