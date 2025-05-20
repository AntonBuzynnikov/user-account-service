package ru.buzynnikov.user_acount_service.services.interfasces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.buzynnikov.user_acount_service.dto.UserResponse;
import ru.buzynnikov.user_acount_service.models.User;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;


public interface UserService {

    UserAuthDTO getUserByEmail(String email);

    void isUserExists(Long id);

    Page<UserResponse> findUser(String email, String name, String phone, String dateOfBirth, Pageable pageable);

    void updateDateOfBird(Long userId, String dateOfBirth);

    void updateName(Long userId, String name);


}
