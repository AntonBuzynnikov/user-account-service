package ru.buzynnikov.user_acount_service.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buzynnikov.user_acount_service.security.models.UserAuthDTO;
import ru.buzynnikov.user_acount_service.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN Email e ON u.id = e.user.id WHERE e.email = :email")
    Optional<User> findByEmail(String email);

    @Transactional(readOnly = true)
    @Query("SELECT new ru.buzynnikov.user_acount_service.security.models.UserAuthDTO(u.id, e.email, u.password) FROM User u JOIN Email e ON u.id = e.user.id WHERE e.email = :email")
    Optional<UserAuthDTO> findByEmailForAuth(String email);
}
