package ru.buzynnikov.user_acount_service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.buzynnikov.user_acount_service.services.interfasces.UserService;

/**
 * Реализация пользовательского сервиса для предоставления подробностей о пользователе.
 * Используется Spring Security для получения информации о пользователе при аутентификации.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Загрузка подробностей пользователя по его имени (email).
     *
     * @param username имя пользователя (электронная почта)
     * @return объект UserDetails с информацией о пользователе
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserByEmail(username);
    }
}
