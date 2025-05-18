package ru.buzynnikov.user_acount_service.services.interfasces;

import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;

public interface UserService {

    public UserAuthDTO getUserByEmail(String email);


}
