package ru.buzynnikov.user_acount_service.services;

import org.springframework.stereotype.Service;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;

import ru.buzynnikov.user_acount_service.exceptions.UserNotFoundException;
import ru.buzynnikov.user_acount_service.repositories.UserRepository;
import ru.buzynnikov.user_acount_service.services.interfasces.UserService;

@Service
public class DefaultUserService implements UserService {


    private final UserRepository userRepository;


    public DefaultUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserAuthDTO getUserByEmail(String email) {
        UserAuthDTO user = userRepository.findByEmailForAuth(email).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        System.out.println(user.getUserId());
        return user;
    }

}
