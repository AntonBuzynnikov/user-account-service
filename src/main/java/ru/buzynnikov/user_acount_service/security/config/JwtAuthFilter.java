package ru.buzynnikov.user_acount_service.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.security.CustomUserDetailsService;
import ru.buzynnikov.user_acount_service.security.JwtService;

import java.io.IOException;

/**
 * Фильтр аутентификации по JWT-токену.
 * Анализирует заголовок Authorization и извлекает оттуда JWT-токен для последующей проверки подлинности пользователя.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER = "Bearer ";
    private final JwtService jwtService;
    private final CustomUserDetailsService detailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService detailsService) {
        this.jwtService = jwtService;
        this.detailsService = detailsService;
    }


    /**
     * Основная логика фильтра аутентификации.
     * Проверяет наличие и валидность JWT-токена, восстанавливает данные пользователя и устанавливает их в контексте безопасности.
     *
     * @param request     объект запроса
     * @param response    объект ответа
     * @param filterChain цепочка дальнейших фильтров
     * @throws ServletException если возникают ошибки сервлета
     * @throws IOException     если возникают ошибки ввода-вывода
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER.length());
        String email = jwtService.extractEmail(token);
        if(!(email == null) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserAuthDTO userAuthDTO = (UserAuthDTO) detailsService.loadUserByUsername(email);
            if(jwtService.validateToken(token, userAuthDTO.getUserId())) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAuthDTO,
                        null,
                        userAuthDTO.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            }

        }
        filterChain.doFilter(request, response);

    }
}
